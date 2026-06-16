package com.solvd.schooltimetablegenerator.dao;

import com.solvd.schooltimetablegenerator.model.Timetable;
import com.solvd.schooltimetablegenerator.model.TimetableEntry;

public interface TimetableDao {

    Timetable insert(Timetable timetable);

    void insertEntry(TimetableEntry entry);

    Timetable findById(Long id);

}
