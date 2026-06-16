package com.solvd.schooltimetablegenerator.dao;

import com.solvd.schooltimetablegenerator.model.Teacher;

import java.util.List;

public interface TeacherDao {

    List<Teacher> findAll();

    Teacher findById(Long id);

}
