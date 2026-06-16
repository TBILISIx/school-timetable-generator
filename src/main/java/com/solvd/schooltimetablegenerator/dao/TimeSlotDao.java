package com.solvd.schooltimetablegenerator.dao;

import com.solvd.schooltimetablegenerator.model.TimeSlot;

import java.util.List;

public interface TimeSlotDao {

    List<TimeSlot> findAll();

    TimeSlot findById(Long id);

    void insert(TimeSlot timeSlot);

    void deleteAll();

}
