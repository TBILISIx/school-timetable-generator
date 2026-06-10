import com.solvd.school.timetable.generator.controller.MainController;
import com.solvd.school.timetable.generator.dao.ClassroomDao;
import com.solvd.school.timetable.generator.dao.SubjectDao;
import com.solvd.school.timetable.generator.dao.TeacherDao;
import com.solvd.school.timetable.generator.dao.impl.MyBatisClassroomDaoImpl;
import com.solvd.school.timetable.generator.dao.impl.MyBatisSubjectDaoImpl;
import com.solvd.school.timetable.generator.dao.impl.MyBatisTeacherDaoImpl;
import com.solvd.school.timetable.generator.model.TimeSlot;
import com.solvd.school.timetable.generator.service.DataLoaderService;
import com.solvd.school.timetable.generator.service.TimeSlotService;
import com.solvd.school.timetable.generator.service.impl.DataLoaderServiceImpl;
import com.solvd.school.timetable.generator.service.impl.TimeSlotServiceImpl;

import java.util.List;

public class Main {

    public static void main(String[] args) {

        // 1. Create the DAO implementations
//        SubjectDao subjectDao = new MyBatisSubjectDaoImpl();
//        TeacherDao teacherDao = new MyBatisTeacherDaoImpl();
//        ClassroomDao classroomDao = new MyBatisClassroomDaoImpl();

        // 2. Call findAll on each and print results
//        System.out.println("=== SUBJECTS ===");
//        subjectDao.findAll().forEach(System.out::println);
//
//        System.out.println("=== TEACHERS ===");
//        teacherDao.findAll().forEach(System.out::println);
//
//        System.out.println("=== CLASSROOMS ===");
//        classroomDao.findAll().forEach(System.out::println);


        //3. Tests to see what is in database

//        DataLoaderService loader = new DataLoaderServiceImpl();
//
//        loader.loadSubjects().forEach(System.out::println);
//        loader.loadTeachers().forEach(System.out::println);
//        loader.loadClassrooms().forEach(System.out::println);
//
//        TimeSlotService timeSlotService = new TimeSlotServiceImpl();
//        List<TimeSlot> slots = timeSlotService.generateAndSave(5);
//        slots.forEach(System.out::println);

        new MainController().run();
    }
}