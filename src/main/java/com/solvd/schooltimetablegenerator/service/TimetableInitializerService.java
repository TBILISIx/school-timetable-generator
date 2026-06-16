package com.solvd.schooltimetablegenerator.service;

import com.solvd.schooltimetablegenerator.model.*;

import java.util.List;

public interface TimetableInitializerService {
    List<Timetable> initializeTable(
            List<TimeSlot> slots,
            List<Subject> subjects,
            List<Teacher> teachers,
            List<Classroom> classrooms,
            int subjectsPerDay,
            int tablesSize
    );
}