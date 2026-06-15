import com.solvd.school.timetable.generator.model.*;
import com.solvd.school.timetable.generator.service.DataLoaderService;
import com.solvd.school.timetable.generator.service.GeneticAlgorithmService;
import com.solvd.school.timetable.generator.service.TimeSlotService;
import com.solvd.school.timetable.generator.service.TimetableInitializerService;
import com.solvd.school.timetable.generator.service.impl.DataLoaderServiceImpl;
import com.solvd.school.timetable.generator.service.impl.GeneticAlgorithmServiceImpl;
import com.solvd.school.timetable.generator.service.impl.TimeSlotServiceImpl;
import com.solvd.school.timetable.generator.service.impl.TimetableInitializerServiceImpl;
import com.solvd.school.timetable.generator.util.PrinterUtils;

import java.util.List;
import java.util.Scanner;

public class Main {

    static DataLoaderService loader = new DataLoaderServiceImpl();
    static TimeSlotService timeSlotService = new TimeSlotServiceImpl();
    static TimetableInitializerService initializer = new TimetableInitializerServiceImpl();
    static GeneticAlgorithmService geneticAlgorithm = new GeneticAlgorithmServiceImpl();

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        // load data first, we need to know how many subjects we have
        // before we can ask the user a sensible question
        List<Subject> subjects = loader.loadSubjects();
        List<Teacher> teachers = loader.loadTeachers();
        List<Classroom> classrooms = loader.loadClassrooms();

        // count regular (non PE) subjects, this is the max amount of
        // different subjects we can fit in a day without repeating one
        long regularSubjectCount = subjects.stream().filter(s -> !s.isPe()).count();
        int maxSubjectsPerDay = (int) regularSubjectCount + 1; // +1 for the PE period

        int subjectsPerDay = readSubjectsPerDay(scanner, maxSubjectsPerDay);

        // generate time slots

        List<TimeSlot> slots = timeSlotService.generateAndSave(subjectsPerDay);

        // initialize population

        List<Timetable> population = initializer.initializeTable(
                slots, subjects, teachers, classrooms, subjectsPerDay, 50);

        // run genetic algorithm

        Timetable best = geneticAlgorithm.evolve(population, subjects, teachers, classrooms);

        // print result

        PrinterUtils.print(best);

        if (best.getFitnessScore() == 0) {
            System.out.println("Optimal timetable found (fitness score: 0).");
        } else {
            System.out.println("Best timetable found, but it still has "
                    + best.getFitnessScore() + " constraint-violation point(s).");
        }

        scanner.close();
    }

    // just keeps asking until the user enters something we can actually use
    private static int readSubjectsPerDay(Scanner scanner, int maxSubjectsPerDay) {
        int value;
        while (true) {
            System.out.printf("Enter number of subjects per day (2-%d): ", maxSubjectsPerDay);
            value = scanner.nextInt();
            if (value >= 2 && value <= maxSubjectsPerDay) {
                return value;
            }
            System.out.printf(
                    "Invalid value. Must be between 2 and %d "
                            + "(last period is always PE, and we only have %d regular subjects).%n",
                    maxSubjectsPerDay, maxSubjectsPerDay - 1);
        }
    }

}
