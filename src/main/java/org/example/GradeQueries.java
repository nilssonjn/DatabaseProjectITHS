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
                1. Add grade for students
                2. Show grades for a student
                3. Show all grades
                """);
        int choice = getChoice();
        switch (choice) {
            case 1 -> addGradeForStudent();
            case 2 -> showStudentGrades();
            case 3 -> showAllGrades();
        }
    }

    public static void showAllGrades() {
        inTransaction(entityManager -> {
            String queryString = """
                    SELECT g FROM Grade g
                    """;
            var query = entityManager.createQuery(queryString, Grade.class);
            List<Grade> grades = query.getResultList();
            grades.forEach(System.out::println);
        });
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
            if (!courses.isEmpty()) {
                setGradeByName(entityManager, courses, result);
            } else {
                System.out.println("No course with that name found");
            }
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
        for (int i = 0; i < result.nrOfStudents(); i++) {
            System.out.println("Enter student name: " + (i + 1) + ": ");
            String studentName = scanner.nextLine();

            List<Student> students = findStudentsByName(entityManager, new scannerInputs("", studentName, "", 0));

            if (!students.isEmpty()) {
                Grade grade = new Grade();
                grade.setGradeCourse(courses.getFirst());
                grade.setGradeStudent(students.getFirst());
                grade.setGradeValue(result.gradeValue());
                entityManager.persist(grade);
                System.out.println("Grade added for " + studentName);
            } else {
                System.out.println("No student with that name found, grade was not added for " + studentName);
            }
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
        try {
            System.out.println("Enter course name: ");
            String courseName = scanner.nextLine();

            System.out.println("Enter amount of students to put grade for: ");
            int nrOfStudents = Integer.parseInt(scanner.nextLine());

            System.out.println("Enter grade: ");
            String gradeValue = scanner.nextLine();

            return new scannerInputs(courseName, "", gradeValue, nrOfStudents);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input for number of students, Please enter a valid number... Returning to inputs...");
        }
        return getScannerInputs();
    }

    private record scannerInputs(String courseName, String studentName, String gradeValue, int nrOfStudents) {
    }
}
