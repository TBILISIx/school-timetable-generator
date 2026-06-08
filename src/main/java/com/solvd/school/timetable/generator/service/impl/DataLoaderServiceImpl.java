package com.solvd.school.timetable.generator.service.impl;

import com.solvd.school.timetable.generator.dao.ClassroomDao;
import com.solvd.school.timetable.generator.dao.SubjectDao;
import com.solvd.school.timetable.generator.dao.TeacherDao;
import com.solvd.school.timetable.generator.dao.impl.MyBatisClassroomDaoImpl;
import com.solvd.school.timetable.generator.dao.impl.MyBatisSubjectDaoImpl;
import com.solvd.school.timetable.generator.dao.impl.MyBatisTeacherDaoImpl;
import com.solvd.school.timetable.generator.model.Classroom;
import com.solvd.school.timetable.generator.model.Subject;
import com.solvd.school.timetable.generator.model.Teacher;
import com.solvd.school.timetable.generator.service.DataLoaderService;

import java.util.List;

public class DataLoaderServiceImpl implements DataLoaderService {

    private final SubjectDao subjectDao     = new MyBatisSubjectDaoImpl();
    private final TeacherDao teacherDao     = new MyBatisTeacherDaoImpl();
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
