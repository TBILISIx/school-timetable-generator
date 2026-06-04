package com.solvd.school.timetable.generator.dao;

import com.solvd.school.timetable.generator.model.TimeSlot;

import java.util.List;

public interface TimeSlotDao {

    List<TimeSlot> findAll();

    TimeSlot findById(Long id);

    void insert(TimeSlot timeSlot);

    void deleteAll();

}
