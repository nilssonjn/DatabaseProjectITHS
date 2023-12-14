package org.example;

import org.example.entities.LanguageCourse;

import java.util.List;

import static org.example.Main.getChoice;
import static org.example.Main.inTransaction;

public class CourseQueries {
    public static void selectCourseQueries() {
        System.out.println("""
                1. Show all courses
                2. Show all courses, course leader and teachers.
                """);
        int choice = getChoice();
        switch (choice){
            case 1 -> showAllCourses();
        }
    }

    public static void showAllCourses() {
        inTransaction(entityManager -> {
            String queryString = """
                    select c from LanguageCourse c
                    """;
            var query = entityManager.createQuery(queryString, LanguageCourse.class);
            List<LanguageCourse> courses = query.getResultList();
            courses.forEach(System.out::println);
        });
    }
}
