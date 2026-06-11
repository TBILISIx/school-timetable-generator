import com.solvd.school.timetable.generator.model.*;
import com.solvd.school.timetable.generator.service.*;
import com.solvd.school.timetable.generator.service.impl.*;
import com.solvd.school.timetable.generator.util.PrinterUtils;

import java.util.List;
import java.util.Scanner;

public class Main {

    static DataLoaderService loader = new DataLoaderServiceImpl();
    static TimeSlotService timeSlotService = new TimeSlotServiceImpl();
    static TimetableInitializerService initializer = new TimetableInitializerServiceImpl();
    static GeneticAlgorithmService geneticAlgorithm = new GeneticAlgorithmServiceImpl();

    public static void main(String[] args) {

        // 1. User input

        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter number of subjects per day: ");
        int subjectsPerDay = scanner.nextInt();

        //  2. Load data from DB

        List<Subject> subjects = loader.loadSubjects();
        List<Teacher> teachers = loader.loadTeachers();
        List<Classroom> classrooms = loader.loadClassrooms();

        // 3. Generate time slots

        List<TimeSlot> slots = timeSlotService.generateAndSave(subjectsPerDay);

        // 4. Initialize population

        List<Timetable> population = initializer.initializePopulation(
                slots, subjects, teachers, classrooms, subjectsPerDay, 50);

        // 5. Run genetic algorithm

        Timetable best = geneticAlgorithm.evolve(
                population, slots, subjects, teachers, classrooms, subjectsPerDay);


        // 6. Print result

        PrinterUtils.print(best);

        scanner.close();
    }

}
