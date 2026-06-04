package com.solvd.school.timetable.generator.dao;

import com.solvd.school.timetable.generator.model.Timetable;

import java.util.List;

public interface TimetableMapper {

    void insert(Timetable timetable);
    Timetable findById(int id);
    List<Timetable> findAll();
}