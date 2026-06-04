package com.solvd.school.timetable.generator.dao;

import com.solvd.school.timetable.generator.model.Timetable;
import com.solvd.school.timetable.generator.model.TimetableEntry;

public interface TimetableDao {

    Timetable insert(Timetable timetable);

    void insertEntry(TimetableEntry entry);

    Timetable findById(Long id);

}
