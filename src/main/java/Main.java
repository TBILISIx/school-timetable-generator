import com.solvd.school.timetable.generator.model.*;
import com.solvd.school.timetable.generator.service.DataLoaderService;
import com.solvd.school.timetable.generator.service.GeneticAlgorithmService;
import com.solvd.school.timetable.generator.service.TimeSlotService;
import com.solvd.school.timetable.generator.service.TimetableInitializerService;
import com.solvd.school.timetable.generator.service.impl.DataLoaderServiceImpl;
import com.solvd.school.timetable.generator.service.impl.GeneticAlgorithmServiceImpl;
import com.solvd.school.timetable.generator.service.impl.TimeSlotServiceImpl;
import com.solvd.school.timetable.generator.service.impl.TimetableInitializerServiceImpl;

import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        // 1. User input
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter number of subjects per day: ");
        int subjectsPerDay = scanner.nextInt();

        //  2. Load data from DB
        DataLoaderService loader = new DataLoaderServiceImpl();
        List<Subject> subjects = loader.loadSubjects();
        List<Teacher> teachers = loader.loadTeachers();
        List<Classroom> classrooms = loader.loadClassrooms();

        // 3. Generate time slots
        TimeSlotService timeSlotService = new TimeSlotServiceImpl();
        List<TimeSlot> slots = timeSlotService.generateAndSave(subjectsPerDay);

        // 4. Initialize population
        TimetableInitializerService initializer = new TimetableInitializerServiceImpl();
        List<Timetable> population = initializer.initializePopulation(
                slots, subjects, teachers, classrooms, subjectsPerDay, 50);

        // 5. Run genetic algorithm
        GeneticAlgorithmService ga = new GeneticAlgorithmServiceImpl();
        Timetable best = ga.evolve(
                population, slots, subjects, teachers, classrooms, subjectsPerDay);

        scanner.close();
    }

}
