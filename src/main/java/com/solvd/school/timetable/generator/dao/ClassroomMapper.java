package com.solvd.school.timetable.generator.dao;

import com.solvd.school.timetable.generator.model.Classroom;

import java.util.List;

public interface ClassroomMapper {

    List<Classroom> findAll();
    void insert(Classroom classroom);
    void delete(int id);
}