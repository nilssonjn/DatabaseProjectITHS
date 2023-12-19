package org.example;

import jakarta.persistence.EntityManager;
import org.example.entities.Grade;
import org.example.entities.LanguageCourse;
import org.example.entities.Student;

import java.util.List;
import java.util.Scanner;

import static org.example.Main.getChoice;
import static org.example.Main.inTransaction;

public class GradeQueries {
    static Scanner scanner = new Scanner(System.in);

    public static void addGradeOptions() {
        System.out.println("""
                Grade options:
                1. Add grade for a student
                2. Show grades for a student
                """);
        int choice = getChoice();
        switch (choice) {
            case 1 -> addGradeForStudent();
            case 2 -> showStudentGrades();
        }
    }

    private static void showStudentGrades() {
        System.out.println("Enter student name: ");
        String studentName = scanner.nextLine();
        inTransaction(entityManager -> {
            String queryString = """
                    SELECT g FROM Grade g
                    WHERE g.gradeStudent.studentName = :studentName
                    """;
            var query = entityManager.createQuery(queryString, Grade.class);
            query.setParameter("studentName", studentName);
            List<Grade> grades = query.getResultList();
            System.out.println("Grades for " + studentName + ":");
            grades.forEach(grade -> System.out.println("Course: " + grade.getGradeCourse().getCourseName() +
                    " Grade: " + grade.getGradeValue()));
        });
    }

    private static void addGradeForStudent() {
        scannerInputs result = getScannerInputs();

        inTransaction(entityManager -> {
            List<LanguageCourse> courses = findCoursesByName(entityManager, result);
            setGradeByName(entityManager, courses, result);
        });
    }

    private static List<LanguageCourse> findCoursesByName(EntityManager entityManager, scannerInputs result) {
        String queryString = """
                SELECT lc FROM LanguageCourse lc
                WHERE lc.courseName = :courseName
                """;
        var query = entityManager.createQuery(queryString, LanguageCourse.class);
        query.setParameter("courseName", result.courseName());
        return query.getResultList();
    }

    private static void setGradeByName(EntityManager entityManager, List<LanguageCourse> courses, scannerInputs result) {
        if (!courses.isEmpty()) {
            List<Student> students = findStudentsByName(entityManager, result);

            Grade grade = new Grade();
            grade.setGradeCourse(courses.getFirst());
            grade.setGradeStudent(students.getFirst());
            grade.setGradeValue(result.gradeValue());
            entityManager.persist(grade);
            System.out.println("Grade added");
        }
    }

    private static List<Student> findStudentsByName(EntityManager entityManager, scannerInputs result) {
        String studentQueryString = """
                SELECT s FROM Student s
                WHERE s.studentName = :studentName
                """;
        var studentQuery = entityManager.createQuery(studentQueryString, Student.class);
        studentQuery.setParameter("studentName", result.studentName());
        return studentQuery.getResultList();
    }

    private static scannerInputs getScannerInputs() {
        System.out.println("Enter course name: ");
        String courseName = scanner.nextLine();

        System.out.println("Enter student name: ");
        String studentName = scanner.nextLine();

        System.out.println("Enter grade: ");
        String gradeValue = scanner.nextLine();

        return new scannerInputs(courseName, studentName, gradeValue);
    }

    private record scannerInputs(String courseName, String studentName, String gradeValue) {
    }
}
