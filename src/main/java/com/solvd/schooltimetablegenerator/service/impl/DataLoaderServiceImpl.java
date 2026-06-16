package com.solvd.schooltimetablegenerator.service.impl;

import com.solvd.schooltimetablegenerator.dao.ClassroomDao;
import com.solvd.schooltimetablegenerator.dao.SubjectDao;
import com.solvd.schooltimetablegenerator.dao.TeacherDao;
import com.solvd.schooltimetablegenerator.dao.impl.MyBatisClassroomDaoImpl;
import com.solvd.schooltimetablegenerator.dao.impl.MyBatisSubjectDaoImpl;
import com.solvd.schooltimetablegenerator.dao.impl.MyBatisTeacherDaoImpl;
import com.solvd.schooltimetablegenerator.model.Classroom;
import com.solvd.schooltimetablegenerator.model.Subject;
import com.solvd.schooltimetablegenerator.model.Teacher;
import com.solvd.schooltimetablegenerator.service.DataLoaderService;

import java.util.List;

public class DataLoaderServiceImpl implements DataLoaderService {

    private final SubjectDao subjectDao = new MyBatisSubjectDaoImpl();
    private final TeacherDao teacherDao = new MyBatisTeacherDaoImpl();
    private final ClassroomDao classroomDao = new MyBatisClassroomDaoImpl();

    @Override
    public List<Subject> loadSubjects() {
        return subjectDao.findAll();
    }

    @Override
    public List<Teacher> loadTeachers() {
        return teacherDao.findAll();
    }

    @Override
    public List<Classroom> loadClassrooms() {
        return classroomDao.findAll();
    }

}
