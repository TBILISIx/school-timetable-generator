import com.solvd.schooltimetablegenerator.model.*;
import com.solvd.schooltimetablegenerator.service.DataLoaderService;
import com.solvd.schooltimetablegenerator.service.GeneticAlgorithmService;
import com.solvd.schooltimetablegenerator.service.TimeSlotService;
import com.solvd.schooltimetablegenerator.service.TimetableInitializerService;
import com.solvd.schooltimetablegenerator.service.impl.DataLoaderServiceImpl;
import com.solvd.schooltimetablegenerator.service.impl.GeneticAlgorithmServiceImpl;
import com.solvd.schooltimetablegenerator.service.impl.TimeSlotServiceImpl;
import com.solvd.schooltimetablegenerator.service.impl.TimetableInitializerServiceImpl;
import com.solvd.schooltimetablegenerator.util.PrinterUtils;

import java.util.List;
import java.util.Scanner;

public class Main {

    static DataLoaderService loader = new DataLoaderServiceImpl();
    static TimeSlotService timeSlotService = new TimeSlotServiceImpl();
    static TimetableInitializerService initializer = new TimetableInitializerServiceImpl();
    static GeneticAlgorithmService geneticAlgorithm = new GeneticAlgorithmServiceImpl();

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        int subjectsPerDay = readSubjectsPerDay(scanner);

        // load data first, we need to know how many subjects we have
        // before we can ask the user a sensible question
        List<Subject> subjects = loader.loadSubjects();
        List<Teacher> teachers = loader.loadTeachers();
        List<Classroom> classrooms = loader.loadClassrooms();

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
    private static int readSubjectsPerDay(Scanner scanner) {
        int value;
        while (true) {
            System.out.print("Enter number of subjects per day (2-9): ");
            value = scanner.nextInt();
            if (value >= 2 && value <= 9) {
                return value;
            }
            System.out.println("Invalid value. Please enter a number between 2 and 9.");
        }
    }

}
