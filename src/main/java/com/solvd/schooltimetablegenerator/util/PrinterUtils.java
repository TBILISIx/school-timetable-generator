package com.solvd.schooltimetablegenerator.util;

import com.solvd.schooltimetablegenerator.model.Timetable;
import com.solvd.schooltimetablegenerator.model.TimetableEntry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PrinterUtils {

    private PrinterUtils() {

    }

    private static final Logger LOGGER =
            LogManager.getLogger(PrinterUtils.class);

    private static final List<String> DAYS = List.of(
            "MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY"
    );

    public static void print(Timetable timetable) {

        // Group entries by day and period for easy lookup
        Map<String, Map<Integer, TimetableEntry>> grid = new LinkedHashMap<>();

        for (TimetableEntry entry : timetable.getEntries()) {
            String day = entry.getSlot().getDayOfWeek();
            Integer period = entry.getSlot().getPeriodNumber();
            grid.computeIfAbsent(day, k -> new LinkedHashMap<>()).put(period, entry);
        }

        // Find max period number
        int maxPeriod = timetable.getEntries().stream()
                .mapToInt(e -> e.getSlot().getPeriodNumber())
                .max()
                .orElse(0);

        // Header
        System.out.println();
        System.out.println("****************************************************************************************************************");
        System.out.println("*                                    GENERATED SCHOOL TIMETABLE                                                *");
        System.out.println("****************************************************************************************************************");
        System.out.println();

        //Column headers (days)
        System.out.printf("%-10s", "PERIOD");
        for (String day : DAYS) {
            System.out.printf("| %-20s", day);
        }
        System.out.println();

        // Separator
        System.out.println("-".repeat(110));

        // Rows (periods)
        for (int period = 1; period <= maxPeriod; period++) {
            // Subject row
            System.out.printf("%-10s", "P" + period);
            for (String day : DAYS) {
                TimetableEntry entry = grid.getOrDefault(day, new LinkedHashMap<>()).get(period);
                String subject = (entry != null) ? entry.getSubject().getName() : "-";
                System.out.printf("| %-20s", truncate(subject, 20));
            }
            System.out.println();

            // Teacher row
            System.out.printf("%-10s", "");
            for (String day : DAYS) {
                TimetableEntry entry = grid.getOrDefault(day, new LinkedHashMap<>()).get(period);
                String teacher = (entry != null) ? entry.getTeacher().getName() : "-";
                System.out.printf("| %-20s", truncate(teacher, 20));
            }
            System.out.println();

            // Classroom row
            System.out.printf("%-10s", "");
            for (String day : DAYS) {
                TimetableEntry entry = grid.getOrDefault(day, new LinkedHashMap<>()).get(period);
                String classroom = (entry != null) ? entry.getClassroom().getName() : "-";
                System.out.printf("| %-20s", truncate(classroom, 20));
            }
            System.out.println();

            //  Row separator
            System.out.println("-".repeat(110));
        }

        System.out.println();
        LOGGER.info("Timetable printed successfully");
    }

    private static String truncate(String value, int maxLength) {
        if (value == null) return "-";
        return value.length() > maxLength ? value.substring(0, maxLength - 1) + "." : value;
    }

}