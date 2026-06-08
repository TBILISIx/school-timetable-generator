package com.solvd.school.timetable.generator.service;

import com.solvd.school.timetable.generator.model.Classroom;
import com.solvd.school.timetable.generator.model.Subject;
import com.solvd.school.timetable.generator.model.Teacher;

import java.util.List;

public interface DataLoaderService {

    List<Subject> loadSubjects();

    List<Teacher> loadTeachers();

    List<Classroom> loadClassrooms();

}
