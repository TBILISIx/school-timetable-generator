package com.solvd.school.timetable.generator.dao;

import com.solvd.school.timetable.generator.model.Subject;

import java.util.List;

public interface SubjectDao {

    List<Subject> findAll();

    Subject findById(Long id);

}
