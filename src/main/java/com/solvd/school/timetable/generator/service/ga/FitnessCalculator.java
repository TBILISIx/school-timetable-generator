package com.solvd.school.timetable.generator.service.ga;

import com.solvd.school.timetable.generator.model.Timetable;
import com.solvd.school.timetable.generator.model.TimetableEntry;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FitnessCalculator {

    // How many points to subtract per violation
    private static final int HARD_PENALTY = 100; // hard constraint violation
    private static final int SOFT_PENALTY = 10;  // soft constraint violation

    /**
     * Scores a timetable. Higher is better.
     * Starts at a perfect score and subtracts penalties for each violation.
     *
     * Hard constraints (big penalty):
     *   H1. PE must be in the last period of the day
     *   H2. Teacher cannot be in two entries at the same time
     *   H3. Classroom cannot be in two entries at the same time
     *
     * Soft constraints (small penalty):
     *   S1. Same subject should not appear twice in one day
     */
    public int calculate(Timetable timetable) {
        List<TimetableEntry> entries = timetable.getEntries();
        int penalty = 0;

        penalty += checkPeLastPeriod(entries, timetable.getSubjectsPerDay());
        penalty += checkTeacherConflicts(entries);
        penalty += checkClassroomConflicts(entries);
        penalty += checkSameSubjectTwiceInOneDay(entries);

        // Score can't go below 0
        int score = Math.max(0, 1000 - penalty);
        timetable.setFitnessScore(score);
        return score;
    }

    // ─── Hard constraint H1 — PE must be last period ─────────────────────────

    private int checkPeLastPeriod(List<TimetableEntry> entries, int periodsPerDay) {
        int penalty = 0;
        for (TimetableEntry entry : entries) {
            if (entry.getSubject().isPe()) {
                // PE is a violation if it is NOT in the last period
                if (entry.getSlot().getPeriodNumber() != periodsPerDay) {
                    penalty += HARD_PENALTY;
                }
            }
        }
        return penalty;
    }

    // ─── Hard constraint H2 — teacher conflict ────────────────────────────────

    private int checkTeacherConflicts(List<TimetableEntry> entries) {
        int penalty = 0;

        // Key: "teacherId_day_period" → count how many entries share that combo
        Map<String, Integer> seen = new HashMap<>();

        for (TimetableEntry entry : entries) {
            String key = entry.getTeacher().getId()
                    + "_" + entry.getSlot().getDayOfWeek()
                    + "_" + entry.getSlot().getPeriodNumber();

            seen.merge(key, 1, Integer::sum);
        }

        // Any key with count > 1 means a teacher is double-booked
        for (int count : seen.values()) {
            if (count > 1) {
                penalty += HARD_PENALTY * (count - 1);
            }
        }

        return penalty;
    }

    // ─── Hard constraint H3 — classroom conflict ─────────────────────────────

    private int checkClassroomConflicts(List<TimetableEntry> entries) {
        int penalty = 0;

        // Key: "classroomId_day_period" → count how many entries share that combo
        Map<String, Integer> seen = new HashMap<>();

        for (TimetableEntry entry : entries) {
            String key = entry.getClassroom().getId()
                    + "_" + entry.getSlot().getDayOfWeek()
                    + "_" + entry.getSlot().getPeriodNumber();

            seen.merge(key, 1, Integer::sum);
        }

        for (int count : seen.values()) {
            if (count > 1) {
                penalty += HARD_PENALTY * (count - 1);
            }
        }

        return penalty;
    }

    // ─── Soft constraint S1 — same subject twice in one day ──────────────────

    private int checkSameSubjectTwiceInOneDay(List<TimetableEntry> entries) {
        int penalty = 0;

        // Key: "subjectId_day" → count how many times subject appears that day
        Map<String, Integer> seen = new HashMap<>();

        for (TimetableEntry entry : entries) {
            String key = entry.getSubject().getId()
                    + "_" + entry.getSlot().getDayOfWeek();

            seen.merge(key, 1, Integer::sum);
        }

        for (int count : seen.values()) {
            if (count > 1) {
                penalty += SOFT_PENALTY * (count - 1);
            }
        }

        return penalty;
    }
}