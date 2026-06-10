package com.solvd.school.timetable.generator.service.impl;

import com.solvd.school.timetable.generator.model.*;
import com.solvd.school.timetable.generator.service.FitnessService;
import com.solvd.school.timetable.generator.service.GeneticAlgorithmService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.stream.Collectors;

public class GeneticAlgorithmServiceImpl implements GeneticAlgorithmService {

    private static final Logger LOGGER =
            LogManager.getLogger(GeneticAlgorithmServiceImpl.class);

    private static final int GENERATIONS = 200;
    private static final double MUTATION_RATE = 0.1;

    private final FitnessService fitnessService = new FitnessServiceImpl();
    private final Random random = new Random();

    @Override
    public Timetable evolve(
            List<Timetable> population,
            List<TimeSlot> slots,
            List<Subject> subjects,
            List<Teacher> teachers,
            List<Classroom> classrooms,
            int subjectsPerDay) {

        //  1. score initial population
        population.forEach(fitnessService::calculateFitness);

        Timetable best = getBest(population);
        LOGGER.info("Generation 0  best score: {}", best.getFitnessScore());

        for (int generation = 1; generation <= GENERATIONS; generation++) {

            //  2.  select top 50% as parents
            List<Timetable> parents = select(population);

            //  3. crossover parents to refill population
            List<Timetable> newPopulation = new ArrayList<>(parents);
            while (newPopulation.size() < population.size()) {
                Timetable parentA = parents.get(random.nextInt(parents.size()));
                Timetable parentB = parents.get(random.nextInt(parents.size()));
                Timetable child = crossover(parentA, parentB, slots, subjects, teachers, classrooms);
                newPopulation.add(child);
            }

            //  4. mutate some children
            for (Timetable timetable : newPopulation) {
                if (random.nextDouble() < MUTATION_RATE) {
                    mutate(timetable, subjects, teachers, classrooms);
                }
            }

            //  5. evaluate new population
            newPopulation.forEach(fitnessService::calculateFitness);
            population = newPopulation;

            // 6. track best
            best = getBest(population);
            LOGGER.info("Generation {}  best score: {}", generation, best.getFitnessScore());

            // early exit if perfect solution found
            if (best.getFitnessScore() == 0) {
                LOGGER.info("Perfect solution found at generation {}", generation);
                break;
            }
        }

        return best;
    }

    //  7. return timetable with lowest fitness score
    private Timetable getBest(List<Timetable> population) {
        return population.stream()
                .min(Comparator.comparingInt(Timetable::getFitnessScore))
                .orElseThrow(() -> new RuntimeException("Population is empty"));
    }

    //  8. select top 50% by fitness score
    private List<Timetable> select(List<Timetable> population) {
        return population.stream()
                .sorted(Comparator.comparingInt(Timetable::getFitnessScore))
                .limit(population.size() / 2)
                .collect(Collectors.toList());
    }

    // 9. combine two parents into one child
    private Timetable crossover(
            Timetable parentA,
            Timetable parentB,
            List<TimeSlot> slots,
            List<Subject> subjects,
            List<Teacher> teachers,
            List<Classroom> classrooms) {

        List<TimetableEntry> entriesA = parentA.getEntries();
        List<TimetableEntry> entriesB = parentB.getEntries();

        // 10. single point crossover - first half from A, second half from B
        int crossoverPoint = entriesA.size() / 2;

        List<TimetableEntry> childEntries = new ArrayList<>();

        for (int i = 0; i < entriesA.size(); i++) {
            TimetableEntry source = (i < crossoverPoint) ? entriesA.get(i) : entriesB.get(i);
            childEntries.add(TimetableEntry.builder()
                    .slot(source.getSlot())
                    .subject(source.getSubject())
                    .teacher(source.getTeacher())
                    .classroom(source.getClassroom())
                    .build());
        }

        // 11. repair PE-last constraint after crossover
        repairPeConstraint(childEntries, subjects, teachers, classrooms);

        return Timetable.builder()
                .entries(childEntries)
                .subjectsPerDay(parentA.getSubjectsPerDay())
                .build();
    }

    // 12.  randomly swap a non-PE entry with a new random one
    private void mutate(
            Timetable timetable,
            List<Subject> subjects,
            List<Teacher> teachers,
            List<Classroom> classrooms) {

        List<TimetableEntry> entries = timetable.getEntries();

        // 13. only mutate period entries that is not last (so PE is reserved)
        List<TimetableEntry> mutableEntries = entries.stream()
                .filter(e -> !e.getSubject().isPe())
                .toList();

        if (mutableEntries.isEmpty()) return;

        // 14. pick a random entry to mutate
        TimetableEntry target = mutableEntries.get(random.nextInt(mutableEntries.size()));

        // 15. pick a new random regular subject
        List<Subject> regularSubjects = subjects.stream()
                .filter(s -> !s.isPe())
                .toList();

        Subject newSubject = regularSubjects.get(random.nextInt(regularSubjects.size()));

        // 16. pick a teacher who can teach the new subject
        List<Teacher> eligibleTeachers = teachers.stream()
                .filter(t -> t.getSubjects().stream()
                        .anyMatch(s -> s.getId().equals(newSubject.getId())))
                .toList();

        if (eligibleTeachers.isEmpty()) return;

        Teacher newTeacher = eligibleTeachers.get(random.nextInt(eligibleTeachers.size()));
        Classroom newClassroom = classrooms.get(random.nextInt(classrooms.size()));

        // 17. apply mutation
        target.setSubject(newSubject);
        target.setTeacher(newTeacher);
        target.setClassroom(newClassroom);
    }

    //  18. fix PE last after crossover
    private void repairPeConstraint(
            List<TimetableEntry> entries,
            List<Subject> subjects,
            List<Teacher> teachers,
            List<Classroom> classrooms) {

        // 19. group by day
        Map<String, List<TimetableEntry>> byDay = new LinkedHashMap<>();
        for (TimetableEntry entry : entries) {
            byDay.computeIfAbsent(entry.getSlot().getDayOfWeek(), k -> new ArrayList<>())
                    .add(entry);
        }

        // 20. find PE subject and teacher
        Subject peSubject = subjects.stream()
                .filter(Subject::isPe)
                .findFirst()
                .orElseThrow();

        Teacher peTeacher = teachers.stream()
                .filter(t -> t.getSubjects().stream().anyMatch(Subject::isPe))
                .findFirst()
                .orElseThrow();

        Classroom peClassroom = classrooms.stream()
                .filter(c -> c.getName().toLowerCase().contains("gym"))
                .findFirst()
                .orElse(classrooms.get(classrooms.size() - 1));

        for (List<TimetableEntry> dayEntries : byDay.values()) {

            // 21. find last period entry for this day
            TimetableEntry lastEntry = dayEntries.stream()
                    .max(Comparator.comparingInt(e -> e.getSlot().getPeriodNumber()))
                    .orElseThrow();

            // 22. force PE into last slot
            lastEntry.setSubject(peSubject);
            lastEntry.setTeacher(peTeacher);
            lastEntry.setClassroom(peClassroom);
        }
    }

}
