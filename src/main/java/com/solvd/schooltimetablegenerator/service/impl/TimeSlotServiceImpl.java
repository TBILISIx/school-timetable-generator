package com.solvd.schooltimetablegenerator.service.impl;

import com.solvd.schooltimetablegenerator.dao.TimeSlotDao;
import com.solvd.schooltimetablegenerator.dao.impl.MyBatisTimeSlotDaoImpl;
import com.solvd.schooltimetablegenerator.model.TimeSlot;
import com.solvd.schooltimetablegenerator.service.TimeSlotService;

import java.util.ArrayList;
import java.util.List;

public class TimeSlotServiceImpl implements TimeSlotService {

    private final TimeSlotDao timeSlotDao = new MyBatisTimeSlotDaoImpl();

    private static final List<String> DAYS = List.of(
            "MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY"
    );

    @Override
    public List<TimeSlot> generateAndSave(int subjectsPerDay) {

        // clears old slots from previous run
        timeSlotDao.deleteAll();

        List<TimeSlot> slots = new ArrayList<>();

        // generates fresh slots with old friend for loop based on users input
        for (String day : DAYS) {
            for (int period = 1; period <= subjectsPerDay; period++) {
                TimeSlot slot = TimeSlot.builder()
                        .dayOfWeek(day)
                        .periodNumber(period)
                        .build();

                // 3. save to database
                timeSlotDao.insert(slot);
                slots.add(slot);
            }
        }
        return slots;
    }

}