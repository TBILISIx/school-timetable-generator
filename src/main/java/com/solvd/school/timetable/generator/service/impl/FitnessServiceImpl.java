package com.solvd.school.timetable.generator.service.impl;

import com.solvd.school.timetable.generator.model.GroupSlot;
import com.solvd.school.timetable.generator.model.Timetable;
import com.solvd.school.timetable.generator.model.TimetableEntry;
import com.solvd.school.timetable.generator.service.FitnessService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
        penalty += checkClassroomSubjectMatch(entries);
        penalty += checkPeIsLast(entries);
        penalty += checkTeacherSubjectMatch(entries);
        penalty += checkSubjectRepeatPerDay(entries);

        timetable.setFitnessScore(penalty);

        LOGGER.debug("Timetable fitness score: {}", penalty);
    }

    //  same teacher double booked in same slot
    private int checkTeacherConflicts(List<TimetableEntry> entries) {
        return entries.stream()
                .collect(Collectors.groupingBy(
                        e -> new GroupSlot(
                                e.getTeacher().getId(),
                                e.getSlot().getId()
                        ),
                        Collectors.counting()
                ))
                .values()
                .stream()
                .filter(count -> count > 1)
                .mapToInt(count -> HARD_PENALTY)
                .sum();
    }

    //  same classroom double booked in same slot
    private int checkClassroomConflicts(List<TimetableEntry> entries) {
        return entries.stream()
                .collect(Collectors.groupingBy(
                        e -> new GroupSlot(
                                e.getClassroom().getId(),
                                e.getSlot().getId()
                        ),
                        Collectors.counting()
                ))
                .values()
                .stream()
                .filter(count -> count > 1)
                .mapToInt(count -> HARD_PENALTY)
                .sum();
    }

    //  non-PE subject assigned to Gym or PE assigned to normal room
    private int checkClassroomSubjectMatch(List<TimetableEntry> entries) {
        return entries.stream()
                .filter(e -> {
                    boolean isGym = e.getClassroom().getName().toLowerCase().contains("gym");
                    boolean isPe = e.getSubject().isPe();
                    return isGym != isPe; // gym should match PE and vice versa
                })
                .mapToInt(e -> HARD_PENALTY)
                .sum();
    }

    //  PE must always be in the last period of each day 
    private int checkPeIsLast(List<TimetableEntry> entries) {

        Map<String, List<TimetableEntry>> byDay = entries.stream()
                .collect(Collectors.groupingBy(
                        e -> e.getSlot().getDayOfWeek(),
                        LinkedHashMap::new,
                        Collectors.toList()
                ));

        return byDay.values().stream()
                .mapToInt(dayEntries -> {
                    int maxPeriod = dayEntries.stream()
                            .mapToInt(e -> e.getSlot().getPeriodNumber())
                            .max()
                            .orElse(0);

                    return dayEntries.stream()
                            .mapToInt(e -> {
                                boolean isPe = e.getSubject().isPe();
                                boolean isLastPeriod = e.getSlot().getPeriodNumber() == maxPeriod;
                                if (isPe && !isLastPeriod) return HARD_PENALTY;
                                if (!isPe && isLastPeriod) return HARD_PENALTY;
                                return 0;
                            })
                            .sum();
                })
                .sum();
    }

    //  Teacher teaching a subject they are not qualified for 
    private int checkTeacherSubjectMatch(List<TimetableEntry> entries) {
        return entries.stream()
                .filter(e -> e.getTeacher().getSubjects().stream()
                        .noneMatch(s -> s.getId().equals(e.getSubject().getId())))
                .mapToInt(e -> HARD_PENALTY)
                .sum();
    }

    //  Same subject appearing more than once per day 
    private int checkSubjectRepeatPerDay(List<TimetableEntry> entries) {
        return entries.stream()
                .collect(Collectors.groupingBy(
                        e -> e.getSlot().getDayOfWeek() + "_" + e.getSubject().getId(),
                        Collectors.counting()
                ))
                .values()
                .stream()
                .filter(count -> count > 1)
                .mapToInt(count -> SOFT_PENALTY)
                .sum();
    }

}