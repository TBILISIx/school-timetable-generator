package com.solvd.school.timetable.generator.dao;

import com.solvd.school.timetable.generator.model.Classroom;

import java.util.List;

public interface ClassroomDao {

    List<Classroom> findAll();

    Classroom findById(Long id);

}
