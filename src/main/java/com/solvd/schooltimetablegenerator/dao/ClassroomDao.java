package com.solvd.schooltimetablegenerator.dao;

import com.solvd.schooltimetablegenerator.model.Classroom;

import java.util.List;

public interface ClassroomDao {

    List<Classroom> findAll();

    Classroom findById(Long id);

}
