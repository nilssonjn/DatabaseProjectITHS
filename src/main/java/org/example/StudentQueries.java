package org.example;

import jakarta.persistence.TypedQuery;
import org.example.entities.LanguageCourse;
import org.example.entities.Student;

import java.util.List;
import java.util.Scanner;

import static org.example.Main.getChoice;
import static org.example.Main.inTransaction;

public class StudentQueries {
    static Scanner scanner = new Scanner(System.in);

    public static void insertStudentQueries() {
        System.out.println("Enter student name:");
        String name = scanner.nextLine();
        System.out.println("Enter student email:");
        String email = scanner.nextLine();
        System.out.println("Enter course ID for student:");
        int courseId = Integer.parseInt(scanner.nextLine());

        inTransaction(entityManager -> {
            LanguageCourse course = entityManager.find(LanguageCourse.class, courseId);
            Student student = new Student();
            student.setStudentName(name);
            student.setStudentEmail(email);
            student.setStudentCourse(course);
            course.addStudent(student);
            entityManager.persist(student);
        });
    }

    public static void showAllStudents() {
        inTransaction(entityManager -> {
            TypedQuery<Student> query = entityManager.createQuery("SELECT s FROM Student s", Student.class);
            List<Student> students = query.getResultList();
            if (students.isEmpty()) {
                System.out.println("No students found.");
            } else {
                for (Student student : students) {
                    System.out.println("ID: " + student.getId() + ", Name: " + student.getStudentName() + ", Email: "
                            + student.getStudentEmail() + ", Course: "
                            + (student.getStudentCourse() != null ? student.getStudentCourse().getCourseName() : "None"));
                }
            }
        });
    }


    public static void updateStudentQueries() {
        System.out.println("Enter ID of student you want to update:");
        int id = getChoice();
        System.out.println("Enter new name for student:");
        String name = scanner.nextLine();
        System.out.println("Enter new email for student:");
        String email = scanner.nextLine();

        inTransaction(entityManager -> {
            Student student = entityManager.find(Student.class, id);
            student.setStudentName(name);
            student.setStudentEmail(email);
            entityManager.persist(student);
        });
    }

    public static void deleteStudentQueries() {
        System.out.println("Enter ID of student you want to delete:");
        int id = getChoice();

        inTransaction(entityManager -> {
            Student student = entityManager.find(Student.class, id);
            LanguageCourse course = entityManager.find(LanguageCourse.class,student.getStudentCourse().getId());
            course.removeStudent(student);
            entityManager.remove(student);
        });
    }


    // statistic queries
    public static void statsStudentQueries() {
        System.out.println("""
                1. Show total number of students
                2. Show number of students in each language course
                3. Show number of students per school
                """);
        System.out.print("Select an option: ");
        int choice = getChoice();
        switch (choice) {
            case 1 -> showTotalNumberOfStudents();
            case 2 -> showNumberOfStudentsPerCourse();
            case 3 -> showNumberOfStudentsPerSchool();
            default -> System.out.println("Invalid option. Please try again.");
        }
    }


    private static void showTotalNumberOfStudents() {
        inTransaction(entityManager -> {
            TypedQuery<Long> query = entityManager.createQuery("SELECT COUNT(s) FROM Student s", Long.class);
            Long totalStudents = query.getSingleResult();
            System.out.println("Total number of students: " + totalStudents);
        });
    }

    private static void showNumberOfStudentsPerCourse() {
        inTransaction(entityManager -> {
            TypedQuery<Object[]> query = entityManager.createQuery(
                    "SELECT c.courseName, COUNT(s) FROM Student s JOIN s.studentCourse c GROUP BY c.courseName",
                    Object[].class
            );
            List<Object[]> results = query.getResultList();
            for (Object[] result : results) {
                System.out.println("Course: " + result[0] + ", Number of Students: " + result[1]);
            }
        });
    }

    private static void showNumberOfStudentsPerSchool() {
        inTransaction(entityManager -> {
            TypedQuery<Object[]> query = entityManager.createQuery(
                    "SELECT sch.schoolName, COUNT(s) FROM Student s JOIN s.studentCourse c JOIN c.courseSchool sch GROUP BY sch.schoolName",
                    Object[].class
            );
            List<Object[]> results = query.getResultList();
            for (Object[] result : results) {
                System.out.println("School: " + result[0] + ", Number of Students: " + result[1]);
            }
        });
    }
}
