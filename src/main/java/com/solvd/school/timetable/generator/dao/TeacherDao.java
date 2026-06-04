package com.solvd.school.timetable.generator.dao;

import com.solvd.school.timetable.generator.model.Teacher;

import java.util.List;

public interface TeacherDao {

    List<Teacher> findAll();

    Teacher findById(Long id);

}
