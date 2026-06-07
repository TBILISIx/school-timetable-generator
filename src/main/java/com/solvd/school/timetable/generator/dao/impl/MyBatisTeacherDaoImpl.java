package com.solvd.school.timetable.generator.dao.impl;

import com.solvd.school.timetable.generator.util.MyBatisSessionHolder;
import com.solvd.school.timetable.generator.dao.TeacherDao;
import com.solvd.school.timetable.generator.model.Teacher;
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