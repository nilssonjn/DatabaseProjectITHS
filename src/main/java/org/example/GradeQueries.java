package org.example;

import java.util.Scanner;

import static org.example.Main.getChoice;

public class GradeQueries {
    static Scanner scanner = new Scanner(System.in);

    public static void addGradeOptions () {
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
        System.out.println("Enter student id: ");
        int id = getId();
        if (id == -1) return;
        System.out.println("Enter grade for the student: ");
    }
}
