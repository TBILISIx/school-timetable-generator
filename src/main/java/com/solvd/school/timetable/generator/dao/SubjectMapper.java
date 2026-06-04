package com.solvd.school.timetable.generator.dao;

import com.solvd.school.timetable.generator.model.Subject;

import java.util.List;

public interface SubjectMapper {
    List<Subject> findAll();
    Subject findById(int id);
    void insert(Subject subject);
    void delete(int id);
}