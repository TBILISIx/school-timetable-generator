package com.solvd.school.timetable.generator.service;

import com.solvd.school.timetable.generator.model.Classroom;
import com.solvd.school.timetable.generator.model.Subject;
import com.solvd.school.timetable.generator.model.Teacher;
import com.solvd.school.timetable.generator.model.Timetable;

import java.util.List;

public interface GeneticAlgorithmService {

    Timetable evolve(
            List<Timetable> population,
            List<Subject> subjects,
            List<Teacher> teachers,
            List<Classroom> classrooms
    );
}
