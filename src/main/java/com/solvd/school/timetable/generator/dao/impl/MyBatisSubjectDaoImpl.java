package com.solvd.school.timetable.generator.dao.impl;

import com.solvd.school.timetable.generator.util.MyBatisSessionHolder;
import com.solvd.school.timetable.generator.dao.SubjectDao;
import com.solvd.school.timetable.generator.model.Subject;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

public class MyBatisSubjectDaoImpl implements SubjectDao {

    @Override
    public List<Subject> findAll() {
        try (SqlSession session = MyBatisSessionHolder.openSession()) {
            SubjectDao mapper = session.getMapper(SubjectDao.class);
            return mapper.findAll();
        }
    }

    @Override
    public Subject findById(Long id) {
        try (SqlSession session = MyBatisSessionHolder.openSession()) {
            SubjectDao mapper = session.getMapper(SubjectDao.class);
            return mapper.findById(id);
        }
    }
}