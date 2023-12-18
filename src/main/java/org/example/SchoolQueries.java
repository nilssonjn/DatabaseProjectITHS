package org.example;

import org.example.entities.School;
import java.util.List;
import java.util.Scanner;
import static org.example.Main.getChoice;
import static org.example.Main.inTransaction;

public class SchoolQueries {
    static Scanner scanner = new Scanner(System.in);

    public static void selectSchoolQueries() {
        System.out.println("""
                School options:
                ===============
                1. Show all schools
                2. Show schools with courses
                                
                """);
        int choice = getChoice();
        switch (choice) {
            case 1 -> showAllSchools();
            case 2 -> schoolsWithCourses();
        }
    }

    public static void schoolStatsQueries() {
        System.out.println("""
                School statistics options:
                ==========================
                1. Show how many schools are saved
                2. Show how many courses a school have
                                
                """);
        int choice = getChoice();
        switch (choice) {
            case 1 -> savedSchoolQueries();
            case 2 -> savedCoursesInASchoolQueries();
        }
    }

    private static void savedCoursesInASchoolQueries() {
        System.out.println("Enter ID of school:");
        int id = getId();
        if (id == -1)
            return;
        inTransaction(entityManager -> {
            School school = entityManager.find(School.class, id);
            if (school == null) {
                System.out.println("Incorrect id, returning to menu");
                return;
            }
            System.out.println("There are " + school.getLanguageCourses().size() +
                    " courses in the " + school.getSchoolName() + " school");
        });
    }

    private static void savedSchoolQueries() {
        inTransaction(entityManager -> {
            String queryString = """
                    SELECT count(s) FROM School s
                    """;
            var query = entityManager.createQuery(queryString, School.class);
            System.out.println("There are " + query.getSingleResult() + " schools saved.");
        });
    }

    private static int getId() {
        int id = getChoice();
        if (id == -1) {
            System.out.println("Returning to menu");
        }
        return id;
    }

    private static void schoolsWithCourses() {
        inTransaction(entityManager -> {
            String queryString = """
                    SELECT DISTINCT s FROM School s
                    LEFT JOIN FETCH s.languageCourses
                    WHERE s.languageCourses IS NOT EMPTY
                    """;
            var query = entityManager.createQuery(queryString, School.class);
            List<School> schools = query.getResultList();
            schools.forEach(school -> {
                System.out.println("School name: " + school.getSchoolName());
                school.getLanguageCourses().forEach(course ->
                        System.out.println("  Course name: " + course.getCourseName())
                );
                System.out.println();
            });
        });
    }

    public static void showAllSchools() {
        inTransaction(entityManager -> {
            String queryString = """
                    SELECT s FROM School s
                    """;
            var query = entityManager.createQuery(queryString, School.class);
            List<School> schools = query.getResultList();
            schools.forEach(System.out::println);
        });
    }

    public static void insertSchoolQueries() {
        inTransaction(entityManager -> {
            System.out.println("Enter school name:");
            String schoolName = scanner.nextLine();
            School school = new School();
            school.setSchoolName(schoolName);
            entityManager.persist(school);
        });
    }

    public static void deleteSchoolQueries() {
        System.out.println("Enter ID of school you want to delete:");
        int id = getId();
        inTransaction(entityManager -> {
            School school = entityManager.find(School.class, id);
            if (school == null) {
                return;
            }
            if (!school.getLanguageCourses().isEmpty()) {
                System.out.println("Can not delete the School since it still has courses connected to it, delete courses first");
                return;
            }
            entityManager.remove(school);
        });
    }

    public static void updateSchoolQueries() {
        System.out.println("Enter ID of the school you want to update:");
        int id = getId();
        if (id == -1)
            return;
        System.out.println("Enter a new name for the school:");
        String schoolName = scanner.nextLine();

        inTransaction(entityManager -> {
            School school = entityManager.find(School.class, id);
            if (school == null) {
                System.out.println("School not found. Returning to menu.");
                return;
            }
            school.setSchoolName(schoolName);
        });
    }
}
