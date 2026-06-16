package com.solvd.schooltimetablegenerator.dao;

import com.solvd.schooltimetablegenerator.model.Subject;

import java.util.List;

public interface SubjectDao {

    List<Subject> findAll();

    Subject findById(Long id);

}
