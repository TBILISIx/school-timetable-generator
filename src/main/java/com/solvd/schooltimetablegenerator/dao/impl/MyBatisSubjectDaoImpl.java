package com.solvd.schooltimetablegenerator.dao.impl;

import com.solvd.schooltimetablegenerator.dao.SubjectDao;
import com.solvd.schooltimetablegenerator.model.Subject;
import com.solvd.schooltimetablegenerator.util.MyBatisSessionHolder;
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