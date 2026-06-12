package com.solvd.school.timetable.generator.service.impl;

import com.solvd.school.timetable.generator.dao.TimeSlotDao;
import com.solvd.school.timetable.generator.dao.TimetableDao;
import com.solvd.school.timetable.generator.dao.impl.MyBatisTimeSlotDaoImpl;
import com.solvd.school.timetable.generator.dao.impl.MyBatisTimetableDaoImpl;
import com.solvd.school.timetable.generator.model.TimeSlot;
import com.solvd.school.timetable.generator.service.TimeSlotService;

import java.util.ArrayList;
import java.util.List;

public class TimeSlotServiceImpl implements TimeSlotService {

    private final TimeSlotDao timeSlotDao = new MyBatisTimeSlotDaoImpl();
    private final TimetableDao timetableDao = new MyBatisTimetableDaoImpl();

    private static final List<String> DAYS = List.of(
            "MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY"
    );

    @Override
    public List<TimeSlot> generateAndSave(int subjectsPerDay) {

        // Delete in correct order — children before parents
        timeSlotDao.disableFkChecks();
        timetableDao.deleteEntries();
        timetableDao.deleteAll();
        timeSlotDao.deleteAll();
        timeSlotDao.enableFkChecks();

        List<TimeSlot> slots = new ArrayList<>();

        for (String day : DAYS) {
            for (int period = 1; period <= subjectsPerDay; period++) {
                TimeSlot slot = TimeSlot.builder()
                        .dayOfWeek(day)
                        .periodNumber(period)
                        .build();
                timeSlotDao.insert(slot);
                slots.add(slot);
            }
        }
        return slots;
    }

    @Override
    public List<TimeSlot> loadAll() {
        return timeSlotDao.findAll();
    }

    @Override
    public void resetAll() {
        // Disable FK checks, delete in order, re-enable
        timeSlotDao.disableFkChecks();
        timeSlotDao.deleteAll();        // time_slots
        timetableDao.deleteAll();       // timetables + entries cascade
        timeSlotDao.enableFkChecks();
    }
}