package com.solvd.schooltimetablegenerator.service;

import com.solvd.schooltimetablegenerator.model.Classroom;
import com.solvd.schooltimetablegenerator.model.Subject;
import com.solvd.schooltimetablegenerator.model.Teacher;

import java.util.List;

public interface DataLoaderService {

    List<Subject> loadSubjects();

    List<Teacher> loadTeachers();

    List<Classroom> loadClassrooms();

}
