package com.solvd.school.timetable.generator.service.ga;

import com.solvd.school.timetable.generator.model.Classroom;
import com.solvd.school.timetable.generator.model.Subject;
import com.solvd.school.timetable.generator.model.Teacher;
import com.solvd.school.timetable.generator.model.TimeSlot;
import com.solvd.school.timetable.generator.model.Timetable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class GeneticAlgorithm {

    private static final Logger LOGGER = LogManager.getLogger(GeneticAlgorithm.class);

    // ─── GA parameters — tune these to change behaviour ─────────────────────
    private static final int    POPULATION_SIZE  = 100;
    private static final int    MAX_GENERATIONS  = 500;
    private static final double MUTATION_RATE    = 0.02; // 2% chance per entry
    private static final int    TOURNAMENT_SIZE  = 5;    // candidates per selection
    private static final int    PERFECT_SCORE    = 1000; // stop early if reached

    private static final Random RANDOM = new Random();

    private final FitnessCalculator fitnessCalculator = new FitnessCalculator();

    /**
     * Runs the full genetic algorithm and returns the best timetable found.
     *
     * Flow per generation:
     *   1. Score every timetable in the population
     *   2. Keep the single best (elitism)
     *   3. Fill the rest of the next generation with:
     *        - select two parents via tournament
     *        - crossover to produce two children
     *        - mutate the children
     *   4. Repeat until MAX_GENERATIONS or perfect score
     */
    public Timetable run(List<Subject>   subjects,
                         List<Teacher>   teachers,
                         List<Classroom> classrooms,
                         List<TimeSlot>  slots,
                         int             periodsPerDay) {

        LOGGER.info("GA started — population={} maxGenerations={}", POPULATION_SIZE, MAX_GENERATIONS);

        // ── Step 1: create initial population ────────────────────────────────
        List<Timetable> population = Chromosome.createPopulation(
                POPULATION_SIZE, subjects, teachers, classrooms, slots, periodsPerDay);

        // ── Step 2: score the initial population ─────────────────────────────
        scoreAll(population);

        Timetable best = getBest(population);
        LOGGER.info("Generation 0 — best score: {}", best.getFitnessScore());

        // ── Step 3: evolve ────────────────────────────────────────────────────
        for (int generation = 1; generation <= MAX_GENERATIONS; generation++) {

            List<Timetable> nextGeneration = new ArrayList<>();

            // Elitism — carry the best timetable straight into the next generation
            // so we never lose the best solution we've found
            nextGeneration.add(best);

            // Fill the rest of the next generation
            while (nextGeneration.size() < POPULATION_SIZE) {

                // Select two parents via tournament selection
                Timetable parentA = tournamentSelect(population);
                Timetable parentB = tournamentSelect(population);

                // Crossover — produce two children
                List<Timetable> children = Chromosome.crossover(parentA, parentB);

                // Mutate each child
                for (Timetable child : children) {
                    Chromosome.mutate(child, MUTATION_RATE);
                    nextGeneration.add(child);
                }
            }

            // Trim to exact population size (crossover can produce one extra)
            if (nextGeneration.size() > POPULATION_SIZE) {
                nextGeneration = nextGeneration.subList(0, POPULATION_SIZE);
            }

            // Score the new generation
            scoreAll(nextGeneration);
            population = nextGeneration;

            // Track the best overall
            Timetable generationBest = getBest(population);
            if (generationBest.getFitnessScore() > best.getFitnessScore()) {
                best = generationBest;
            }

            // Log progress every 50 generations
            if (generation % 50 == 0) {
                LOGGER.info("Generation {} — best score: {}", generation, best.getFitnessScore());
            }

            // Early stop if perfect score reached
            if (best.getFitnessScore() >= PERFECT_SCORE) {
                LOGGER.info("Perfect score reached at generation {}", generation);
                break;
            }
        }

        LOGGER.info("GA finished — best score: {}", best.getFitnessScore());
        return best;
    }

    // ─── Tournament selection ─────────────────────────────────────────────────
    // Picks TOURNAMENT_SIZE random candidates and returns the best one.
    // Better timetables are more likely to be selected but not guaranteed —
    // this keeps diversity in the population.
    private Timetable tournamentSelect(List<Timetable> population) {
        Timetable best = null;
        for (int i = 0; i < TOURNAMENT_SIZE; i++) {
            Timetable candidate = population.get(RANDOM.nextInt(population.size()));
            if (best == null || candidate.getFitnessScore() > best.getFitnessScore()) {
                best = candidate;
            }
        }
        return best;
    }

    // ─── Score every timetable in the population ──────────────────────────────
    private void scoreAll(List<Timetable> population) {
        for (Timetable timetable : population) {
            fitnessCalculator.calculate(timetable);
        }
    }

    // ─── Get the highest scoring timetable ───────────────────────────────────
    private Timetable getBest(List<Timetable> population) {
        return population.stream()
                .max(Comparator.comparingInt(Timetable::getFitnessScore))
                .orElseThrow();
    }
}