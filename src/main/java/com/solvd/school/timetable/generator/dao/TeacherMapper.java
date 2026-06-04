package com.solvd.school.timetable.generator.dao;

import com.solvd.school.timetable.generator.model.Teacher;

import java.util.List;

public interface TeacherMapper {
    List<Teacher> findAll();
    Teacher findById(int id);
    void insert(Teacher teacher);
    void delete(int id);
}