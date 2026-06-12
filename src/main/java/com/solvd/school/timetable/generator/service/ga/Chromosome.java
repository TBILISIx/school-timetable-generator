package com.solvd.school.timetable.generator.service.ga;

import com.solvd.school.timetable.generator.model.Classroom;
import com.solvd.school.timetable.generator.model.Subject;
import com.solvd.school.timetable.generator.model.Teacher;
import com.solvd.school.timetable.generator.model.TimeSlot;
import com.solvd.school.timetable.generator.model.Timetable;
import com.solvd.school.timetable.generator.model.TimetableEntry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Chromosome {

    private static final Random RANDOM = new Random();

    /**
     * Builds one random timetable — one chromosome in the GA population.
     *
     * For every time slot it picks:
     *   - a random subject
     *   - a random teacher who can teach that subject
     *   - a random classroom
     *
     * No constraints are checked here — the fitness calculator handles that.
     * The goal is just to produce a complete, randomly filled timetable.
     */
    public static Timetable createRandom(List<Subject>   subjects,
                                         List<Teacher>   teachers,
                                         List<Classroom> classrooms,
                                         List<TimeSlot>  slots,
                                         int             subjectsPerDay) {

        List<TimetableEntry> entries = new ArrayList<>();

        for (TimeSlot slot : slots) {
            // 1. Pick a random subject
            Subject subject = subjects.get(RANDOM.nextInt(subjects.size()));

            // 2. Pick a random teacher who can teach this subject
            //    Filter teachers down to only those who have this subject
            List<Teacher> qualified = getQualifiedTeachers(teachers, subject);
            Teacher teacher;
            if (qualified.isEmpty()) {
                // Fallback: pick any teacher if none is assigned to this subject
                teacher = teachers.get(RANDOM.nextInt(teachers.size()));
            } else {
                teacher = qualified.get(RANDOM.nextInt(qualified.size()));
            }

            // 3. Pick a random classroom
            Classroom classroom = classrooms.get(RANDOM.nextInt(classrooms.size()));

            // 4. Build the entry — one gene
            TimetableEntry entry = new TimetableEntry();
            entry.setSlot(slot);
            entry.setSubject(subject);
            entry.setTeacher(teacher);
            entry.setClassroom(classroom);

            entries.add(entry);
        }

        Timetable timetable = new Timetable();
        timetable.setEntries(entries);
        timetable.setSubjectsPerDay(subjectsPerDay);
        timetable.setFitnessScore(0);

        return timetable;
    }

    /**
     * Creates a population of N random chromosomes.
     */
    public static List<Timetable> createPopulation(int             size,
                                                   List<Subject>   subjects,
                                                   List<Teacher>   teachers,
                                                   List<Classroom> classrooms,
                                                   List<TimeSlot>  slots,
                                                   int             subjectsPerDay) {
        List<Timetable> population = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            population.add(createRandom(subjects, teachers, classrooms, slots, subjectsPerDay));
        }
        return population;
    }

    /**
     * Crossover — takes two parent timetables and produces two children.
     *
     * Single-point crossover on the flat list of entries:
     *   Child 1 = Parent A's first half + Parent B's second half
     *   Child 2 = Parent B's first half + Parent A's second half
     */
    public static List<Timetable> crossover(Timetable parentA, Timetable parentB) {
        List<TimetableEntry> entriesA = parentA.getEntries();
        List<TimetableEntry> entriesB = parentB.getEntries();

        int size = entriesA.size();
        int cutPoint = RANDOM.nextInt(size - 1) + 1; // somewhere between 1 and size-1

        List<TimetableEntry> childEntriesA = new ArrayList<>();
        childEntriesA.addAll(entriesA.subList(0, cutPoint));
        childEntriesA.addAll(entriesB.subList(cutPoint, size));

        List<TimetableEntry> childEntriesB = new ArrayList<>();
        childEntriesB.addAll(entriesB.subList(0, cutPoint));
        childEntriesB.addAll(entriesA.subList(cutPoint, size));

        Timetable childA = new Timetable();
        childA.setEntries(childEntriesA);
        childA.setSubjectsPerDay(parentA.getSubjectsPerDay());

        Timetable childB = new Timetable();
        childB.setEntries(childEntriesB);
        childB.setSubjectsPerDay(parentA.getSubjectsPerDay());

        return List.of(childA, childB);
    }

    /**
     * Mutation — with a given probability, swaps two random entries in the timetable.
     * This stops the population from getting stuck with the same genes forever.
     */
    public static void mutate(Timetable timetable, double mutationRate) {
        List<TimetableEntry> entries = timetable.getEntries();
        for (int i = 0; i < entries.size(); i++) {
            if (RANDOM.nextDouble() < mutationRate) {
                // Swap this entry with a random other entry
                int swapIndex = RANDOM.nextInt(entries.size());
                Collections.swap(entries, i, swapIndex);
            }
        }
    }

    // ─── Private helpers ─────────────────────────────────────────────────────

    private static List<Teacher> getQualifiedTeachers(List<Teacher> teachers, Subject subject) {
        List<Teacher> qualified = new ArrayList<>();
        for (Teacher teacher : teachers) {
            for (Subject s : teacher.getSubjects()) {
                if (s.getId().equals(subject.getId())) {
                    qualified.add(teacher);
                    break;
                }
            }
        }
        return qualified;
    }
}