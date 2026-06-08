package com.solvd.school.timetable.generator.service;

import com.solvd.school.timetable.generator.model.TimeSlot;

import java.util.List;

public interface TimeSlotService {
    List<TimeSlot> generateAndSave(int subjectsPerDay);
}