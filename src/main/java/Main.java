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

        // 1. User input

        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter number of subjects per day: ");
        int subjectsPerDay = scanner.nextInt();

        if (subjectsPerDay < 10 & subjectsPerDay > 0) {

            //  2. Load data from DB

            List<Subject> subjects = loader.loadSubjects();
            List<Teacher> teachers = loader.loadTeachers();
            List<Classroom> classrooms = loader.loadClassrooms();

            // 3. Generate time slots

            List<TimeSlot> slots = timeSlotService.generateAndSave(subjectsPerDay);

            // 4. Initialize tables

            List<Timetable> table = initializer.initializeTable(
                    slots, subjects, teachers, classrooms, subjectsPerDay, 100);

            // 5. Run genetic algorithm

            Timetable best = geneticAlgorithm.evolve(
                    table, slots, subjects, teachers, classrooms, subjectsPerDay);

            // 6. Print result

            PrinterUtils.print(best);

        } else {

            System.out.println("Invalid number of subjects per day. Please enter a number between 0 and 10.");
        }
        scanner.close();
    }

}
