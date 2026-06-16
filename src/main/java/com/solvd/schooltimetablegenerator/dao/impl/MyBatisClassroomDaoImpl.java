package com.solvd.schooltimetablegenerator.dao.impl;

import com.solvd.schooltimetablegenerator.dao.ClassroomDao;
import com.solvd.schooltimetablegenerator.model.Classroom;
import com.solvd.schooltimetablegenerator.util.MyBatisSessionHolder;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

public class MyBatisClassroomDaoImpl implements ClassroomDao {

    @Override
    public List<Classroom> findAll() {
        try (SqlSession session = MyBatisSessionHolder.openSession()) {
            ClassroomDao mapper = session.getMapper(ClassroomDao.class);
            return mapper.findAll();
        }
    }

    @Override
    public Classroom findById(Long id) {
        try (SqlSession session = MyBatisSessionHolder.openSession()) {
            ClassroomDao mapper = session.getMapper(ClassroomDao.class);
            return mapper.findById(id);
        }
    }
}