package com.solvd.school.timetable.generator.dao;

import com.solvd.school.timetable.generator.model.Timetable;
import com.solvd.school.timetable.generator.model.TimetableEntry;

import java.util.List;

public interface TimetableDao {

    void insert(Timetable timetable);

    void insertEntry(TimetableEntry entry);

    Timetable findById(Long id);

    List<Timetable> findAll();

    void deleteEntries();

    void deleteAll();;
}