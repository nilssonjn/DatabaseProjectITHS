package org.example;

import org.example.entities.Grade;
import org.example.entities.LanguageCourse;
import org.example.entities.Student;
import org.example.entities.Teacher;

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
                """);
        int choice = getChoice();
        switch (choice) {
            case 1 -> addGradeForStudent();
        }
    }

    private static int getId() {
        int id = getChoice();
        if (id == -1) {
            System.out.println("Returning to menu");
        }
        return id;
    }

    private static void addGradeForStudent() {
        scannerInputs result = getScannerInputs();

        inTransaction(entityManager -> {
            String queryString = """
                    SELECT lc FROM LanguageCourse lc
                    WHERE lc.courseName = :courseName
                    """;
            var query = entityManager.createQuery(queryString, LanguageCourse.class);
            query.setParameter("courseName", result.courseName());
            List<LanguageCourse> courses = query.getResultList();

            if (!courses.isEmpty()) {
                Student student = entityManager.find(Student.class, result.studentName());
                if (student != null) {
                    Grade grade = new Grade();
                    grade.setGradeCourse(courses.getFirst());
                    grade.setGradeStudent(student);
                    grade.setGradeValue(result.gradeValue());
                    entityManager.persist(grade);
                    System.out.println("Grade added");
                } else {
                    System.out.println("Student not found with that name: " + result.studentName());
                }
            } else {
                System.out.println("Course not found with name: " + result.courseName());
            }
        });
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
