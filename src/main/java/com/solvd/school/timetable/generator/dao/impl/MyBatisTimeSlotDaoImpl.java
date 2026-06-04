package com.solvd.school.timetable.generator.dao.impl;

import com.solvd.school.timetable.generator.util.MyBatisSessionHolder;
import com.solvd.school.timetable.generator.dao.TimeSlotDao;
import com.solvd.school.timetable.generator.model.TimeSlot;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

public class MyBatisTimeSlotDaoImpl implements TimeSlotDao {

    @Override
    public List<TimeSlot> findAll() {
        try (SqlSession session = MyBatisSessionHolder.openSession()) {
            TimeSlotDao mapper = session.getMapper(TimeSlotDao.class);
            return mapper.findAll();
        }
    }

    @Override
    public TimeSlot findById(Long id) {
        try (SqlSession session = MyBatisSessionHolder.openSession()) {
            TimeSlotDao mapper = session.getMapper(TimeSlotDao.class);
            return mapper.findById(id);
        }
    }

    @Override
    public void insert(TimeSlot timeSlot) {
        try (SqlSession session = MyBatisSessionHolder.openSession()) {
            TimeSlotDao mapper = session.getMapper(TimeSlotDao.class);
            mapper.insert(timeSlot);
        }
    }

    @Override
    public void deleteAll() {
        try (SqlSession session = MyBatisSessionHolder.openSession()) {
            TimeSlotDao mapper = session.getMapper(TimeSlotDao.class);
            mapper.deleteAll();
        }
    }
}