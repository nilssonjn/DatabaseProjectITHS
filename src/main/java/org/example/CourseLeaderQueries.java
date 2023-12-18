package org.example;


import org.example.entities.CourseLeader;
import org.example.entities.LanguageCourse;

import java.util.*;

import static org.example.Main.getChoice;
import static org.example.Main.inTransaction;

public class CourseLeaderQueries {
    static Scanner scanner = new Scanner(System.in);

    public static void statsLeaderQueries() {
        System.out.println("""
                    1. Show number of course leaders and their names
                    2. Show number of course leaders at a school
                """);

        int choice = getChoice();
        switch (choice) {
            case 1 -> nrOfLeaders();
            case 2 -> nrOfLeadersAtSchool();
        }
    }

    public static void selectLeaderQueries() {
        System.out.println("""
                    1. Show Leader Name and Email
                    2. Show Leader for a course
                """);

        int choice = getChoice();
        switch (choice) {
            case 1 -> showCourseLeadersNameAndEmail();
            case 2 -> showCourseLeaderForLanguage();
        }
    }

    public static void insertLeaderQueries(){
        System.out.println("Enter name for new course leader First and Last name: ");
        String name = scanner.nextLine();
        System.out.println("Enter new Email for course leader: ");
        String email = scanner.nextLine();
        System.out.println("Enter the course Id for course leader: ");
        int id = getId();

        inTransaction(entityManager -> {
            LanguageCourse course = entityManager.find(LanguageCourse.class,id);
            CourseLeader leader = new CourseLeader();
            leader.setCourseLeaderName(name);
            leader.setCourseLeaderEmail(email);
            leader.setCourseLeaderCourse(course);
            if (course != null)
                course.addCourseLeader(leader);
            entityManager.persist(leader);
        });
    }
    public static void updateLeaderQueries() {
        System.out.println("Enter ID of the course leader you would like to update:");
        int id = getId();
        scanner.nextLine();

        inTransaction(entityManager -> {
            CourseLeader leader = entityManager.find(CourseLeader.class, id);
            if (leader != null) {
                System.out.println("Enter new full name of the course leader:");
                String name = scanner.nextLine();
                leader.setCourseLeaderName(name);

                System.out.println("Enter new email for the course leader:");
                String email = scanner.nextLine();
                leader.setCourseLeaderEmail(email);

                System.out.println("Enter new course ID for the course leader:");
                int courseId = getId();
                if (leader.getCourseLeaderCourse()!= null && courseId != leader.getCourseLeaderCourse().getId()) {
                    var oldCourse = entityManager.find(LanguageCourse.class,leader.getCourseLeaderCourse().getId());
                    if (oldCourse != null)
                        oldCourse.removeCourseLeader(leader);
                }
                LanguageCourse course = entityManager.find(LanguageCourse.class, courseId);
                if (course != null) {
                    leader.setCourseLeaderCourse(course);
                    course.addCourseLeader(leader);
                    entityManager.merge(leader);
                    System.out.println("Course leader information updated successfully.");
                } else {
                    System.out.println("Course ID not found. Update failed.");
                }
            } else {
                System.out.println("Course leader ID not found. Update failed.");
            }
        });
    }
    private static void showCourseLeaderForLanguage() {
        System.out.println("Enter the language you want to find course leader for: ");
        String courseLanguage = scanner.nextLine();
        inTransaction(entityManager -> {
            String queryString = """
            SELECT cl.courseLeaderName, cl.courseLeaderEmail
            FROM CourseLeader cl
            WHERE cl.courseLeaderCourse.courseName = :courseLanguage
            """;

            var query = entityManager.createQuery(queryString);
            query.setParameter("courseLanguage", courseLanguage);

            List<Object[]> results = query.getResultList();

            System.out.println("Course Leaders for " + courseLanguage + ":");
            for (Object[] result : results) {
                String courseLeaderName = (String) result[0];
                String courseLeaderEmail = (String) result[1];

                System.out.println("Name: " + courseLeaderName + ", Email: " + courseLeaderEmail);
            }
        });
    }
    private static void showCourseLeadersNameAndEmail() {
        inTransaction(entityManager -> {
            String queryString = "SELECT cl.id, cl.courseLeaderName, cl.courseLeaderEmail FROM CourseLeader cl";
            var query = entityManager.createQuery(queryString, Object[].class);
            List<Object[]> results = query.getResultList();

            System.out.println("Course Leaders:");
            for (Object[] result : results) {
                int courseLeaderId = (int) result[0];
                String courseLeaderName = (String) result[1];
                String courseLeaderEmail = (String) result[2];
                System.out.println("ID: "+courseLeaderId+", Name: " + courseLeaderName + ", Email: " + courseLeaderEmail);
            }
        });
    }

    private static void nrOfLeaders() {
        inTransaction(entityManager -> {
            String queryString = """
                select cl.courseLeaderName, count(cl) from CourseLeader cl
                group by cl.courseLeaderName
                """;
            var query = entityManager.createQuery(queryString, Object[].class);
            List<Object[]> results = query.getResultList();

            System.out.println("There are " + results.size() + " course leaders in the database");
            for (Object[] result : results) {
                String courseLeaderName = (String) result[0];
                System.out.println("CourseLeader: " + courseLeaderName);
            }
        });
    }
    private static void nrOfLeadersAtSchool() {
        inTransaction(entityManager -> {
            String queryString = """
            SELECT s.schoolName, COUNT(DISTINCT cl.courseLeaderId) AS NumberOfCourseLeaders
            FROM LanguageCourse lc
            JOIN School s ON lc.courseSchoolId = s.schoolId
            JOIN courseLeader cl ON lc.courseId = cl.courseLeaderCourseId
            GROUP BY s.schoolName;
            """;
            var query = entityManager.createNativeQuery(queryString);
            List<Object[]> results = query.getResultList();

            System.out.println("Number of Course Leaders at Each School:");
            for (Object[] result : results) {
                String schoolName = (String) result[0];
                Long count = ((Number) result[1]).longValue();
                System.out.println("School: " + schoolName + ", Count: " + count);
            }
        });
    }

    public static void deleteLeaderQueries(){
        System.out.println("Enter ID of leader you want to remove");
        int id = getId();
        inTransaction(entityManager -> {
            CourseLeader leader = entityManager.find(CourseLeader.class,id);
            if(leader != null) {
                entityManager.remove(leader);
                System.out.println("Course leader have successfully been removed! ");
            }
            else {
                System.out.println("Course leader not found! ");
            }
        });
    }
    private static int getId() {
        int id = 0;
        boolean isValidInput = false;

        do {
            try {
                String input = scanner.next();
                id = Integer.parseInt(input);
                isValidInput = true;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a number");
            }
        } while (!isValidInput);

        return id;
    }

}
