package com.solvd.schooltimetablegenerator.dao.impl;

import com.solvd.schooltimetablegenerator.dao.TeacherDao;
import com.solvd.schooltimetablegenerator.model.Teacher;
import com.solvd.schooltimetablegenerator.util.MyBatisSessionHolder;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

public class MyBatisTeacherDaoImpl implements TeacherDao {

    @Override
    public List<Teacher> findAll() {
        try (SqlSession session = MyBatisSessionHolder.openSession()) {
            TeacherDao mapper = session.getMapper(TeacherDao.class);
            return mapper.findAll();
        }
    }

    @Override
    public Teacher findById(Long id) {
        try (SqlSession session = MyBatisSessionHolder.openSession()) {
            TeacherDao mapper = session.getMapper(TeacherDao.class);
            return mapper.findById(id);
        }
    }
}