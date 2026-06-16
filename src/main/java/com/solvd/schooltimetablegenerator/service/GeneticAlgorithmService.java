package com.solvd.schooltimetablegenerator.service;

import com.solvd.schooltimetablegenerator.model.Classroom;
import com.solvd.schooltimetablegenerator.model.Subject;
import com.solvd.schooltimetablegenerator.model.Teacher;
import com.solvd.schooltimetablegenerator.model.Timetable;

import java.util.List;

public interface GeneticAlgorithmService {

    Timetable evolve(
            List<Timetable> population,
            List<Subject> subjects,
            List<Teacher> teachers,
            List<Classroom> classrooms
    );
}
