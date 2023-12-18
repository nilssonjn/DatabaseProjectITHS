package org.example;

import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import org.example.entities.LanguageCourse;
import org.example.entities.Teacher;

import java.util.List;
import java.util.Scanner;
import static org.example.Main.getChoice;
import static org.example.Main.inTransaction;

public class TeacherQueries {
    static Scanner scanner = new Scanner(System.in);

    public static void selectTeacherQueries() {
        System.out.println("""
                1. Show all teachers and their contact information
                2. Show teacher and their courses
                3. Show students in teacher class // not done
                """);
        int choice = getChoice();
        switch (choice) {
            case 1 -> showAllTeacherWithContactInfo();
            case 2 -> showTeachersAndCourses();
            case 3 -> System.out.println("Teacher3 method is not complete");
        }
    }

    public static void showAllTeacherWithContactInfo() {
        inTransaction(entityManager -> {
            TypedQuery<Teacher> query = entityManager.createQuery(
                    "SELECT t FROM Teacher t", Teacher.class);
            List<Teacher> teachers = query.getResultList();

            for (Teacher teacher : teachers) {
                System.out.println(teacher.toString());
            }
        });
    }

    public static void showTeachersAndCourses() {
        inTransaction(entityManager -> {
            TypedQuery<Teacher> query = entityManager.createQuery(
                    "SELECT t FROM Teacher t LEFT JOIN FETCH t.teacherCourse", Teacher.class);
            List<Teacher> teachers = query.getResultList();

            // Iterate over all teachers
            for (Teacher teacher : teachers) {
                LanguageCourse course = teacher.getTeacherCourse();
                if (course != null) {
                    System.out.println("Teacher Name: " + teacher.getTeacherName() +
                            ", Course Name: " + course.getCourseName());
                } else {
                    System.out.println("Teacher Name: " + teacher.getTeacherName() + " is not associated with any course.");
                }
            }
        });
    }

    public static void insertTeacherQueries() {
        System.out.println("Enter teacher name:");
        String name = scanner.nextLine();
        System.out.println("Enter teacher e-mail:");
        String email = scanner.nextLine();
        System.out.println("Enter course Id");
        int courseId = Integer.parseInt(scanner.nextLine());

        try {
            inTransaction(entityManager -> {
                LanguageCourse course = entityManager.find(LanguageCourse.class, courseId);
                Teacher teacher = new Teacher();
                teacher.setTeacherName(name);
                teacher.setTeacherEmail(email);
                teacher.setTeacherCourse(course);
                if (course != null)
                    course.addTeacher(teacher);
                entityManager.persist(teacher);
                System.out.println("Teacher " + name + " successfully added.");
            });
        } catch (Exception e) {
            System.out.println("An error occurred while adding the teacher: " + e.getMessage());
        }
    }

    public static void updateTeacherQueries() {
        System.out.println("Enter ID of teacher you want to update:");
        int id = getChoice();
        if (id == -1) {
            return;
        }

        try {
            inTransaction(entityManager -> {
                Teacher teacher = entityManager.find(Teacher.class, id);
                if (teacher == null) {
                    throw new IllegalStateException("Teacher with ID " + id + " not found");
                }

                System.out.println("Enter new name for teacher:");
                String name = scanner.nextLine();
                System.out.println("Enter new email for teacher:");
                String email = scanner.nextLine();
                System.out.println("Enter new course Id");
                int courseId = Integer.parseInt(scanner.nextLine());
                if (teacher.getTeacherCourse() != null && courseId != teacher.getTeacherCourse().getId()) {
                    var oldCourse = entityManager.find(LanguageCourse.class,teacher.getTeacherCourse().getId());
                    if (oldCourse != null)
                        oldCourse.removeTeacher(teacher);
                }

                LanguageCourse course = entityManager.find(LanguageCourse.class, courseId);

                teacher.setTeacherName(name);
                teacher.setTeacherEmail(email);
                teacher.setTeacherCourse(course);
                if (course != null)
                    course.addTeacher(teacher);
                entityManager.persist(teacher);
                System.out.println("Teacher information" + name + " was successfully updated.");
            });
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }}

    public static void deleteTeacherQueries() {
        System.out.println("Enter ID of teacher you want to delete");
        int id = getChoice();
        inTransaction(entityManager -> {
            Teacher teacher = entityManager.find(Teacher.class, id);
            if (teacher != null) {
                entityManager.remove(teacher);
                if (teacher.getTeacherCourse() != null) {
                    LanguageCourse course = entityManager.find(LanguageCourse.class,teacher.getTeacherCourse().getId());
                    course.removeTeacher(teacher);
                }

                System.out.println("Teacher with ID " + id + " was deleted.");
            } else {
                System.out.println("Teacher with ID " + id + " not found.");
                return;
            }
        });
    }

    public static void statsTeacherQueries() {
        System.out.println("""
                1. Show total number of teachers
                2. Show number of students in teacher class
                """);
        int choice = getChoice();
        switch (choice) {
            case 1 -> showTotalNumberOfTeachers();
            case 2 -> countStudentsForTeacherCourse();
            case 3 -> System.out.println("TeacherStat3");
        }

    }
    private static void showTotalNumberOfTeachers() {
        inTransaction(entityManager -> {
            TypedQuery<Long> query = entityManager.createQuery("SELECT COUNT(t) FROM Teacher t", Long.class);
            Long totalTeachers = query.getSingleResult();
            System.out.println("Total number of teachers: " + totalTeachers);
        });
    }

    public static void countStudentsForTeacherCourse() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the teacher's name: ");
        String teacherName = scanner.nextLine();

        try {
            inTransaction(entityManager -> {
                TypedQuery<LanguageCourse> courseQuery = entityManager.createQuery(
                        "SELECT t.teacherCourse FROM Teacher t WHERE t.teacherName = :teacherName", LanguageCourse.class);
                courseQuery.setParameter("teacherName", teacherName);
                LanguageCourse course = courseQuery.getSingleResult();

                if (course == null) {
                    System.out.println("The teacher " + teacherName + " is not associated with any course.");
                    return;
                }

                TypedQuery<Long> countQuery = entityManager.createQuery(
                        "SELECT COUNT(s) FROM Student s WHERE s.studentCourse = :course", Long.class);
                countQuery.setParameter("course", course);
                Long studentCount = countQuery.getSingleResult();

                System.out.println("Course: " + course.getCourseName() +
                        " taught by " + teacherName +
                        " has " + studentCount + " students.");
            });
        } catch (NoResultException e) {
            System.out.println("No matching course found for the teacher's name.");
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }}

