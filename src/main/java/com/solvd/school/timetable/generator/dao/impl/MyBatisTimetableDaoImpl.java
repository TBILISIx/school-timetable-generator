package com.solvd.school.timetable.generator.dao.impl;

import com.solvd.school.timetable.generator.util.MyBatisSessionHolder;
import com.solvd.school.timetable.generator.dao.TimetableDao;
import com.solvd.school.timetable.generator.model.Timetable;
import com.solvd.school.timetable.generator.model.TimetableEntry;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

public class MyBatisTimetableDaoImpl implements TimetableDao {

    @Override
    public void insert(Timetable timetable) {
        try (SqlSession session = MyBatisSessionHolder.openManagedSession()) {
            try {
                TimetableDao mapper = session.getMapper(TimetableDao.class);

                mapper.insert(timetable);

                for (TimetableEntry entry : timetable.getEntries()) {
                    entry.setTimetableId(timetable.getId());
                    mapper.insertEntry(entry);
                }

                session.commit();

            } catch (Exception e) {
                session.rollback();
                throw new RuntimeException("Failed to save timetable", e);
            }
        }
    }

    @Override
    public void insertEntry(TimetableEntry entry) {
        try (SqlSession session = MyBatisSessionHolder.openSession()) {
            TimetableDao mapper = session.getMapper(TimetableDao.class);
            mapper.insertEntry(entry);
        }
    }

    @Override
    public Timetable findById(Long id) {
        try (SqlSession session = MyBatisSessionHolder.openSession()) {
            TimetableDao mapper = session.getMapper(TimetableDao.class);
            return mapper.findById(id);
        }
    }

    @Override
    public List<Timetable> findAll() {
        try (SqlSession session = MyBatisSessionHolder.openSession()) {
            TimetableDao mapper = session.getMapper(TimetableDao.class);
            return mapper.findAll();
        }
    }

    @Override
    public void deleteAll() {
        try (SqlSession session = MyBatisSessionHolder.openSession()) {
            TimetableDao mapper = session.getMapper(TimetableDao.class);
            mapper.deleteAll();
        }
    }

    @Override
    public void deleteEntries() {
        try (SqlSession session = MyBatisSessionHolder.openSession()) {
            TimetableDao mapper = session.getMapper(TimetableDao.class);
            mapper.deleteEntries();
        }
    }
}