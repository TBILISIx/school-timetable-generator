package com.solvd.school.timetable.generator.dao;

import com.solvd.school.timetable.generator.model.TimetableEntry;

import java.util.List;

public interface TimetableEntryMapper {

    void insertAll(List<TimetableEntry> entries);
    List<TimetableEntry> findByTimetableId(int timetableId);
}