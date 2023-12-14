package org.example;

import org.example.entities.LanguageCourse;
import org.example.entities.School;

import java.util.*;

import static org.example.Main.getChoice;
import static org.example.Main.inTransaction;

public class LanguageCourseQueries {
    static Scanner scanner = new Scanner(System.in);
    public static void statsCourseQueries() {
        System.out.println("""
                1. Show number of saved courses
                2. Show number of students in a course
                3. Show number of exams in a course
                """);
        int choice = getChoice();
        switch (choice){
            case 1 -> nrOfSavedCourses();
            case 2 -> nrOfStudentsInACourse();
            case 3 -> nrOfExamsInACourse();
        }
    }

    private static void nrOfExamsInACourse() {
        System.out.println("Enter ID of course");
        int id = getId();
        inTransaction(entityManager -> {
            LanguageCourse course = entityManager.find(LanguageCourse.class,id);
            System.out.println("There are "+course.getExaminations().size()+" examinations in the "+course.getCourseName()+" course");
        });
    }

    private static void nrOfStudentsInACourse() {
        System.out.println("Enter ID of course");
        int id = getId();
        inTransaction(entityManager -> {
            LanguageCourse course = entityManager.find(LanguageCourse.class,id);
            System.out.println("There are "+course.getStudents().size()+" students in the "+course.getCourseName()+" course");
        });
    }

    private static void nrOfSavedCourses() {
        inTransaction(entityManager -> {
            String queryString = """
                    select count(lc) from LanguageCourse lc
                    """;
            var query = entityManager.createQuery(queryString, LanguageCourse.class);
            System.out.println("There are " + query.getSingleResult()+" courses saved in the database");
        });
    }

    public static void deleteCourseQueries() {
        System.out.println("Enter ID of course you want to delete");
        int id = getId();
        inTransaction(entityManager -> {
            LanguageCourse course = entityManager.find(LanguageCourse.class,id);
            entityManager.remove(course);
        });
    }

    public static void updateCourseQueries() {
        System.out.println("Enter ID of course you want to update");
        int id = getId();
        System.out.println("Enter new name for course:");
        String name = scanner.nextLine();
        System.out.println("Enter new start date for course:");
        String startDate = scanner.nextLine();
        System.out.println("Enter new end date for course:");
        String endDate = scanner.nextLine();
        System.out.println("Enter new school ID for course:");
        int schoolId = getId();


        inTransaction(entityManager -> {
            LanguageCourse course = entityManager.find(LanguageCourse.class,id);
            School school = entityManager.find(School.class,schoolId);

            course.setCourseName(name);
            course.setCourseStartDate(startDate);
            course.setCourseEndDate(endDate);
            course.setCourseSchool(school);
            entityManager.persist(course);
        });


    }

    public static void insertCourseQueries() {
        System.out.println("Enter a course name:");
        String name = scanner.nextLine();
        System.out.println("Enter course start date:");
        String startDate = scanner.nextLine();
        System.out.println("Enter course end date:");
        String endDate = scanner.nextLine();
        System.out.println("Enter school ID");
        int id = getId();

        inTransaction(entityManager -> {
            School school = entityManager.find(School.class,id);
            LanguageCourse course = new LanguageCourse();
            course.setCourseName(name);
            course.setCourseStartDate(startDate);
            course.setCourseEndDate(endDate);
            course.setCourseSchool(school);
            entityManager.persist(course);
        });

    }

    private static int getId() {
        int id = getChoice();
        if (id == 100)
            id = 0;
        return id;
    }

    public static void selectCourseQueries() {
        System.out.println("""
                1. Show all courses
                2. Show all courses, course leaders and teachers.
                3. Search for a course
                4. See registered students in a course
                5. See examinations in a course
                """);
        int choice = getChoice();
        switch (choice){
            case 1 -> showAllCourses();
            case 2 -> showAllCoursesWithLeaderAndTeacher();
            case 3 -> searchForCourse();
            case 4 -> findStudentsInACourse();
            case 5 -> findExamsInACourse();
        }
    }

    private static void findExamsInACourse() {
        System.out.print("Enter course name: ");
        String name = scanner.nextLine();

        inTransaction(entityManager -> {
            String queryString = """
                    select c from LanguageCourse c where c.courseName like :name
                    """;
            var query = entityManager.createQuery(queryString, LanguageCourse.class);
            query.setParameter("name", name);
            LanguageCourse course = query.getSingleResult();
            course.getExaminations().forEach(System.out::println);
        });
    }

    private static void findStudentsInACourse() {
        System.out.print("Enter course name: ");
        String name = scanner.nextLine();

        inTransaction(entityManager -> {
            String queryString = """
                    select c from LanguageCourse c where c.courseName like :name
                    """;
            var query = entityManager.createQuery(queryString, LanguageCourse.class);
            query.setParameter("name", name);
            LanguageCourse course = query.getSingleResult();
            course.getStudents().forEach(System.out::println);
        });
    }

    private static void searchForCourse() {
        System.out.print("Enter course name: ");
        String name = scanner.nextLine();

        inTransaction(entityManager -> {
            String queryString = """
                    select c from LanguageCourse c where c.courseName like :name
                    """;
            var query = entityManager.createQuery(queryString, LanguageCourse.class);
            query.setParameter("name", name);
            LanguageCourse course = query.getSingleResult();
            System.out.println(course);
        });
    }

    private static void showAllCoursesWithLeaderAndTeacher() {
        inTransaction(entityManager -> {
            String queryString = """
                    select lc from LanguageCourse lc
                    """;
            var query = entityManager.createQuery(queryString, LanguageCourse.class);
            List<LanguageCourse> courses = query.getResultList();
            courses.stream().map(course -> course.toString() +" - "+ course.getTeachers() +" - "+ course.getCourseLeaders()).forEach(System.out::println);
        });
    }

    public static void showAllCourses() {
        inTransaction(entityManager -> {
            String queryString = """
                    select lc from LanguageCourse lc
                    """;
            var query = entityManager.createQuery(queryString, LanguageCourse.class);
            List<LanguageCourse> courses = query.getResultList();
            courses.forEach(System.out::println);
        });
    }
}