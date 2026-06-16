package com.solvd.schooltimetablegenerator.service;

import com.solvd.schooltimetablegenerator.model.TimeSlot;

import java.util.List;

public interface TimeSlotService {
    List<TimeSlot> generateAndSave(int subjectsPerDay);
}