import com.solvd.school.timetable.generator.dao.ClassroomDao;
import com.solvd.school.timetable.generator.dao.SubjectDao;
import com.solvd.school.timetable.generator.dao.TeacherDao;
import com.solvd.school.timetable.generator.dao.impl.MyBatisClassroomDaoImpl;
import com.solvd.school.timetable.generator.dao.impl.MyBatisSubjectDaoImpl;
import com.solvd.school.timetable.generator.dao.impl.MyBatisTeacherDaoImpl;

public class Main {

    public static void main(String[] args) {

        // 1. Create the DAO implementations
        SubjectDao subjectDao = new MyBatisSubjectDaoImpl();
        TeacherDao teacherDao = new MyBatisTeacherDaoImpl();
        ClassroomDao classroomDao = new MyBatisClassroomDaoImpl();

        // 2. Call findAll on each and print results
        System.out.println("=== SUBJECTS ===");
        subjectDao.findAll().forEach(System.out::println);

        System.out.println("=== TEACHERS ===");
        teacherDao.findAll().forEach(System.out::println);

        System.out.println("=== CLASSROOMS ===");
        classroomDao.findAll().forEach(System.out::println);
    }
}