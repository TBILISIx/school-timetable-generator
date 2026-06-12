package com.solvd.school.timetable.generator.controller;

import com.solvd.school.timetable.generator.dao.TimetableDao;
import com.solvd.school.timetable.generator.dao.impl.MyBatisTimetableDaoImpl;
import com.solvd.school.timetable.generator.model.Classroom;
import com.solvd.school.timetable.generator.model.Subject;
import com.solvd.school.timetable.generator.model.Teacher;
import com.solvd.school.timetable.generator.model.TimeSlot;
import com.solvd.school.timetable.generator.model.Timetable;
import com.solvd.school.timetable.generator.service.DataLoaderService;
import com.solvd.school.timetable.generator.service.TimeSlotService;
import com.solvd.school.timetable.generator.service.ga.GeneticAlgorithm;
import com.solvd.school.timetable.generator.service.impl.DataLoaderServiceImpl;
import com.solvd.school.timetable.generator.service.impl.TimeSlotServiceImpl;
import com.solvd.school.timetable.generator.view.ConsoleView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class MainController {

    private static final Logger LOGGER = LogManager.getLogger(MainController.class);

    private final ConsoleView view = new ConsoleView();
    private final DataLoaderService dataLoaderService = new DataLoaderServiceImpl();
    private final TimeSlotService timeSlotService = new TimeSlotServiceImpl();
    private final GeneticAlgorithm gaService = new GeneticAlgorithm();
    private final TimetableDao timetableDao = new MyBatisTimetableDaoImpl();

    private List<Subject> subjects;
    private List<Teacher> teachers;
    private List<Classroom> classrooms;
    private List<TimeSlot> timeSlots;
    private int periodsPerDay = 0;

    public void run() {
        LOGGER.info("Application started");

        subjects = dataLoaderService.loadSubjects();
        teachers = dataLoaderService.loadTeachers();
        classrooms = dataLoaderService.loadClassrooms();

        boolean running = true;

        while (running) {
            view.printMainMenu();
            int choice = view.readInt("Enter choice");

            switch (choice) {
                case 1 -> handleViewSubjects();
                case 2 -> handleViewTeachers();
                case 3 -> handleViewClassrooms();
                case 4 -> handleSetPeriods();
                case 5 -> handleGenerateTimetable();
                case 6 -> handleViewSavedTimetables();
                case 7 -> {
                    view.printMessage("Goodbye!");
                    LOGGER.info("Application exited");
                    running = false;
                }
                case 8 -> handleReset();
                default -> view.printError("Invalid choice. Enter a number between 1 and 8.");
            }
        }
    }

    private void handleReset() {
        String confirm = view.readString("This deletes all timetables and time slots. Type YES to confirm");
        if (!confirm.equals("YES")) {
            view.printMessage("Cancelled.");
            return;
        }
        timeSlotService.resetAll();
        periodsPerDay = 0;
        timeSlots = null;
        view.printSuccess("All data reset. You can now set periods per day again.");
    }

    // ─── Handlers ────────────────────────────────────────────────────────────

    private void handleViewSubjects() {
        view.printSubjects(subjects);
    }

    private void handleViewTeachers() {
        view.printTeachers(teachers);
    }

    private void handleViewClassrooms() {
        view.printClassrooms(classrooms);
    }

    private void handleSetPeriods() {
        if (periodsPerDay != 0) {
            view.printError("Periods already set. Restart the app to change them.");
            return;
        }
        periodsPerDay = view.readInt("Enter number of periods per day");
        timeSlots = timeSlotService.generateAndSave(periodsPerDay);
        view.printTimeSlots(timeSlots);
        view.printSuccess("Time slots saved. " + timeSlots.size() + " slots created.");
    }

    private void handleGenerateTimetable() {
        if (periodsPerDay == 0) {
            view.printError("Please set periods per day first (option 4).");
            return;
        }

        view.printMessage("Running genetic algorithm, please wait...");

        // 1. Run the GA
        Timetable best = gaService.run(subjects, teachers, classrooms, timeSlots, periodsPerDay);

        // 2. Save to DB
        timetableDao.insert(best);
        view.printSuccess("Timetable saved to database with id=" + best.getId());

        // 3. Display ASCII grid
        view.printTimetable(best, timeSlots);
    }

    private void handleViewSavedTimetables() {
        List<Timetable> all = timetableDao.findAll();
        view.printSavedTimetables(all);

        if (all.isEmpty()) return;

        Long id = (long) view.readInt("Enter timetable ID to view (0 to cancel)");
        if (id == 0) return;

        Timetable timetable = timetableDao.findById(id);
        if (timetable == null) {
            view.printError("Timetable not found.");
            return;
        }

        // Load current time slots from DB for display
        List<TimeSlot> slots = timeSlotService.loadAll();
        view.printTimetable(timetable, slots);
    }
}