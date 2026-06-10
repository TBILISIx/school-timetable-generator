package com.solvd.school.timetable.generator.service.impl;

import com.solvd.school.timetable.generator.model.Timetable;
import com.solvd.school.timetable.generator.model.TimetableEntry;
import com.solvd.school.timetable.generator.service.FitnessService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FitnessServiceImpl implements FitnessService {

    private static final Logger LOGGER =
            LogManager.getLogger(FitnessServiceImpl.class);

    private static final int HARD_PENALTY = 10;
    private static final int SOFT_PENALTY = 5;

    @Override
    public void calculateFitness(Timetable timetable) {
        int penalty = 0;

        List<TimetableEntry> entries = timetable.getEntries();

        penalty += checkTeacherConflicts(entries);
        penalty += checkClassroomConflicts(entries);
        penalty += checkPeIsLast(entries);
        penalty += checkTeacherSubjectMatch(entries);
        penalty += checkSubjectRepeatPerDay(entries);

        timetable.setFitnessScore(penalty);

        LOGGER.debug("Timetable fitness score: {}", penalty);

    }

    //  Same teacher double booked in same slot
    private int checkTeacherConflicts(List<TimetableEntry> entries) {
        int penalty = 0;

        for (int i = 0; i < entries.size(); i++) {
            for (int j = i + 1; j < entries.size(); j++) {
                TimetableEntry a = entries.get(i);
                TimetableEntry b = entries.get(j);

                boolean sameSlot    = a.getSlot().getId().equals(b.getSlot().getId());
                boolean sameTeacher = a.getTeacher().getId().equals(b.getTeacher().getId());

                if (sameSlot && sameTeacher) {
                    penalty += HARD_PENALTY;
                }
            }
        }
        return penalty;
    }

    // Same classroom double booked in same slot
    private int checkClassroomConflicts(List<TimetableEntry> entries) {
        int penalty = 0;

        for (int i = 0; i < entries.size(); i++) {
            for (int j = i + 1; j < entries.size(); j++) {
                TimetableEntry a = entries.get(i);
                TimetableEntry b = entries.get(j);

                boolean sameSlot      = a.getSlot().getId().equals(b.getSlot().getId());
                boolean sameClassroom = a.getClassroom().getId().equals(b.getClassroom().getId());

                if (sameSlot && sameClassroom) {
                    penalty += HARD_PENALTY;
                }
            }
        }
        return penalty;
    }

    // PE last on all day
    private int checkPeIsLast(List<TimetableEntry> entries) {
        int penalty = 0;

        Map<String, List<TimetableEntry>> byDay = new LinkedHashMap<>();
        for (TimetableEntry entry : entries) {
            byDay.computeIfAbsent(entry.getSlot().getDayOfWeek(), k -> new ArrayList<>())
                    .add(entry);
        }

        for (List<TimetableEntry> dayEntries : byDay.values()) {

            int maxPeriod = dayEntries.stream()
                    .mapToInt(e -> e.getSlot().getPeriodNumber())
                    .max()
                    .orElse(0);

            for (TimetableEntry entry : dayEntries) {
                boolean isPe         = entry.getSubject().isPe();
                boolean isLastPeriod = entry.getSlot().getPeriodNumber() == maxPeriod;

                if (isPe && !isLastPeriod)  penalty += HARD_PENALTY;
                if (!isPe && isLastPeriod)  penalty += HARD_PENALTY;
            }
        }
        return penalty;
    }

    // Teacher teaching a subject they are not qualified for penalty
    private int checkTeacherSubjectMatch(List<TimetableEntry> entries) {
        int penalty = 0;

        for (TimetableEntry entry : entries) {
            Long subjectId = entry.getSubject().getId();

            boolean canTeach = entry.getTeacher().getSubjects().stream()
                    .anyMatch(s -> s.getId().equals(subjectId));

            if (!canTeach) {
                penalty += HARD_PENALTY;
            }
        }
        return penalty;
    }

    //  Same subject appearing more than once per day
    private int checkSubjectRepeatPerDay(List<TimetableEntry> entries) {
        int penalty = 0;

        Map<String, List<TimetableEntry>> byDay = new LinkedHashMap<>();
        for (TimetableEntry entry : entries) {
            byDay.computeIfAbsent(entry.getSlot().getDayOfWeek(), k -> new ArrayList<>())
                    .add(entry);
        }

        for (List<TimetableEntry> dayEntries : byDay.values()) {

            Map<Long, Long> subjectCount = dayEntries.stream()
                    .collect(Collectors.groupingBy(
                            e -> e.getSubject().getId(),
                            Collectors.counting()
                    ));

            for (Long count : subjectCount.values()) {
                if (count > 1) {
                    penalty += (int) (SOFT_PENALTY * (count - 1));
                }
            }
        }
        return penalty;
    }
}