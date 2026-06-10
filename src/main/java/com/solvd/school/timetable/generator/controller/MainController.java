package com.solvd.school.timetable.generator.controller;

import com.solvd.school.timetable.generator.model.Classroom;
import com.solvd.school.timetable.generator.model.Subject;
import com.solvd.school.timetable.generator.model.Teacher;
import com.solvd.school.timetable.generator.model.TimeSlot;
import com.solvd.school.timetable.generator.service.DataLoaderService;
import com.solvd.school.timetable.generator.service.TimeSlotService;
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
    private final TimeSlotService timeSlotService     = new TimeSlotServiceImpl();

    // Held in memory once loaded — reused across menu choices
    private List<Subject>   subjects;
    private List<Teacher>   teachers;
    private List<Classroom> classrooms;
    private List<TimeSlot>  timeSlots;
    private int periodsPerDay = 0;

    public void run() {
        LOGGER.info("Application started");

        // Load subjects, teachers, classrooms from DB once at startup
        subjects   = dataLoaderService.loadSubjects();
        teachers   = dataLoaderService.loadTeachers();
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
                case 6 -> {
                    view.printMessage("Goodbye!");
                    LOGGER.info("Application exited");
                    running = false;
                }
                default -> view.printError("Invalid choice. Enter a number between 1 and 6.");
            }
        }
    }

    // ─── Handlers

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
        view.printMessage("Timetable generation");
    }

}