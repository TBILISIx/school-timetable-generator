package com.solvd.schooltimetablegenerator.dao.impl;

import com.solvd.schooltimetablegenerator.dao.TimetableDao;
import com.solvd.schooltimetablegenerator.model.Timetable;
import com.solvd.schooltimetablegenerator.model.TimetableEntry;
import com.solvd.schooltimetablegenerator.util.MyBatisSessionHolder;
import org.apache.ibatis.session.SqlSession;

public class MyBatisTimetableDaoImpl implements TimetableDao {

    /**
     * Saves the timetable header row first, then saves every entry linked
     * to it — all inside one transaction. If anything fails the whole save
     * is rolled back so you never end up with a timetable that has no entries.
     */
    @Override
    public Timetable insert(Timetable timetable) {
        // openManagedSession = autoCommit OFF, we control commit/rollback
        try (SqlSession session = MyBatisSessionHolder.openManagedSession()) {
            try {
                TimetableDao mapper = session.getMapper(TimetableDao.class);

                // 1. Insert the timetable row — MyBatis writes the generated id
                //    back into timetable.id because of useGeneratedKeys="true"
                mapper.insert(timetable);

                // 2. Now that we have the id, stamp it on every entry and save
                for (TimetableEntry entry : timetable.getEntries()) {
                    entry.setTimetableId(timetable.getId());
                    mapper.insertEntry(entry);
                }

                session.commit();
                return timetable;

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
}