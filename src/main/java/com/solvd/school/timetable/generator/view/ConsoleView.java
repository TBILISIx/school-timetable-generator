package com.solvd.school.timetable.generator.view;

import com.solvd.school.timetable.generator.model.Classroom;
import com.solvd.school.timetable.generator.model.Subject;
import com.solvd.school.timetable.generator.model.Teacher;
import com.solvd.school.timetable.generator.model.TimeSlot;
import com.solvd.school.timetable.generator.model.Timetable;
import com.solvd.school.timetable.generator.model.TimetableEntry;

import java.util.List;
import java.util.Scanner;

public class ConsoleView {

    private final Scanner scanner = new Scanner(System.in);

    // ─── Generic input/output

    public void printMessage(String message) {
        System.out.println(message);
    }

    public void printSuccess(String message) {
        System.out.println("[OK] " + message);
    }

    public void printError(String message) {
        System.out.println("[ERROR] " + message);
    }

    public String readString(String prompt) {
        System.out.print(prompt + ": ");
        return scanner.nextLine().trim();
    }

    public int readInt(String prompt) {
        while (true) {
            System.out.print(prompt + ": ");
            String input = scanner.nextLine().trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                printError("Please enter a valid number.");
            }
        }
    }

    // ─── Menus

    public void printMainMenu() {
        System.out.println();
        System.out.println("================================");
        System.out.println("   SCHOOL TIMETABLE GENERATOR  ");
        System.out.println("================================");
        System.out.println("1. View subjects");
        System.out.println("2. View teachers");
        System.out.println("3. View classrooms");
        System.out.println("4. Set periods per day");
        System.out.println("5. Generate timetable");
        System.out.println("6. Exit");
        System.out.println("================================");
    }

    // ─── Display lists

    public void printSubjects(List<Subject> subjects) {
        System.out.println("\n--- SUBJECTS ---");
        for (Subject s : subjects) {
            String pe = s.isPe() ? " [PE]" : "";
            System.out.println("  " + s.getId() + ". " + s.getName() + pe);
        }
    }

    public void printTeachers(List<Teacher> teachers) {
        System.out.println("\n--- TEACHERS ---");
        for (Teacher t : teachers) {
            System.out.println("  " + t.getId() + ". " + t.getName());
            for (Subject s : t.getSubjects()) {
                System.out.println("       can teach: " + s.getName());
            }
        }
    }

    public void printClassrooms(List<Classroom> classrooms) {
        System.out.println("\n--- CLASSROOMS ---");
        for (Classroom c : classrooms) {
            System.out.println("  " + c.getId() + ". " + c.getName());
        }
    }

    public void printTimeSlots(List<TimeSlot> slots) {
        System.out.println("\n--- TIME SLOTS GENERATED ---");
        for (TimeSlot s : slots) {
            System.out.println("  " + s.getDayOfWeek() + " - Period " + s.getPeriodNumber());
        }
    }

    // ─── Timetable grid

    public void printTimetable(Timetable timetable, List<TimeSlot> allSlots) {
        System.out.println("\n========== TIMETABLE (fitness=" + timetable.getFitnessScore() + ") ==========");

        // Get distinct days in order
        List<String> days = allSlots.stream()
                .map(TimeSlot::getDayOfWeek)
                .distinct()
                .toList();

        // Get distinct period numbers in order
        List<Integer> periods = allSlots.stream()
                .map(TimeSlot::getPeriodNumber)
                .distinct()
                .sorted()
                .toList();

        // Print header row
        System.out.printf("%-10s", "Period");
        for (String day : days) {
            System.out.printf("%-30s", day);
        }
        System.out.println();
        System.out.println("-".repeat(10 + days.size() * 30));

        // Print each period row
        for (int period : periods) {
            System.out.printf("%-10s", period);
            for (String day : days) {
                // Find the entry matching this day + period
                TimetableEntry match = timetable.getEntries().stream()
                        .filter(e -> e.getSlot().getDayOfWeek().equals(day)
                                && e.getSlot().getPeriodNumber() == period)
                        .findFirst()
                        .orElse(null);

                if (match == null) {
                    System.out.printf("%-30s", "-");
                } else {
                    String cell = match.getSubject().getName()
                            + "/" + match.getTeacher().getName()
                            + "/" + match.getClassroom().getName();
                    // Truncate if too long for the column
                    if (cell.length() > 28) cell = cell.substring(0, 28);
                    System.out.printf("%-30s", cell);
                }
            }
            System.out.println();
        }

        System.out.println("=".repeat(10 + days.size() * 30));
    }

}