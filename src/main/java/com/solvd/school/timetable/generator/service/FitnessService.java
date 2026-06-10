package com.solvd.school.timetable.generator.service;

import com.solvd.school.timetable.generator.model.Timetable;

public interface FitnessService {
    void calculateFitness(Timetable timetable);
}
