package com.solvd.school.timetable.generator.service.impl;

import com.solvd.school.timetable.generator.model.*;
import com.solvd.school.timetable.generator.service.TimetableInitializerService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

public class TimetableInitializerServiceImpl implements TimetableInitializerService {

    private static final Logger LOGGER =
            LogManager.getLogger(TimetableInitializerServiceImpl.class);

    private final Random random = new Random();

    @Override
    public List<Timetable> initializePopulation(
            List<TimeSlot> slots,
            List<Subject> subjects,
            List<Teacher> teachers,
            List<Classroom> classrooms,
            int subjectsPerDay,
            int populationSize) {

        List<Timetable> population = new ArrayList<>();

        for (int i = 0; i < populationSize; i++) {
            Timetable timetable = createRandomTimetable(
                    slots, subjects, teachers, classrooms, subjectsPerDay);
            population.add(timetable);
        }

        LOGGER.info("Initialized population of {} timetables", populationSize);
        return population;
    }

    private Timetable createRandomTimetable(
            List<TimeSlot> slots,
            List<Subject> subjects,
            List<Teacher> teachers,
            List<Classroom> classrooms,
            int subjectsPerDay) {

        List<TimetableEntry> entries = new ArrayList<>();

        // group slots by day so PE can be placed last per day
        Map<String, List<TimeSlot>> slotsByDay = new LinkedHashMap<>();
        for (TimeSlot slot : slots) {
            slotsByDay
                    .computeIfAbsent(slot.getDayOfWeek(), k -> new ArrayList<>())
                    .add(slot);
        }

        // separate PE subject from regular subjects
        List<Subject> regularSubjects = subjects.stream()
                .filter(s -> !s.isPe())
                .toList();

        Subject peSubject = subjects.stream()
                .filter(Subject::isPe)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No PE subject found in DB"));

        Teacher peTeacher = teachers.stream()
                .filter(t -> t.getSubjects().stream().anyMatch(Subject::isPe))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No PE teacher found in DB"));

        Classroom peClassroom = classrooms.stream()
                .filter(c -> c.getName().toLowerCase().contains("gym"))
                .findFirst()
                .orElse(classrooms.get(classrooms.size() - 1));

        List<Classroom> normalClassrooms = classrooms.stream()
                .filter(c -> !c.getName().toLowerCase().contains("gym"))
                .toList();

        // build entries day by day
        for (Map.Entry<String, List<TimeSlot>> dayEntry : slotsByDay.entrySet()) {

            List<TimeSlot> daySlots = dayEntry.getValue();
            daySlots.sort(Comparator.comparing(TimeSlot::getPeriodNumber));

            for (int i = 0; i < daySlots.size(); i++) {
                TimeSlot slot = daySlots.get(i);
                boolean isLastPeriod = (i == daySlots.size() - 1);

                Subject subject;
                Teacher teacher;
                Classroom classroom;

                if (isLastPeriod) {
                    subject = peSubject;
                    teacher = peTeacher;
                    classroom = peClassroom;
                } else {
                    subject = regularSubjects.get(random.nextInt(regularSubjects.size()));

                    Subject finalSubject = subject;
                    List<Teacher> eligibleTeachers = teachers.stream()
                            .filter(t -> t.getSubjects().stream()
                                    .anyMatch(s -> s.getId().equals(finalSubject.getId())))
                            .toList();

                    teacher = eligibleTeachers.get(random.nextInt(eligibleTeachers.size()));
                    classroom = normalClassrooms.get(random.nextInt(normalClassrooms.size()));
                }

                entries.add(TimetableEntry.builder()
                        .slot(slot)
                        .subject(subject)
                        .teacher(teacher)
                        .classroom(classroom)
                        .build());
            }
        }

        return Timetable.builder()
                .entries(entries)
                .subjectsPerDay(subjectsPerDay)
                .build();
    }

}