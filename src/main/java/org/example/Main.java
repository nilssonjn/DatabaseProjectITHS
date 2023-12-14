package org.example;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.Scanner;
import java.util.function.Consumer;

public class Main {
    private static final EntityManager em = JPAUtil.getEntityManager();
    private static final Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) {
        int choice;
        printChoices();
        menuLoop:
        do {
            System.out.println("\nAwaiting input(5 for options):");
            choice = getChoice();
            switch (choice) {
                case 0 -> {
                    System.out.println("Exiting program");
                    break menuLoop;
                }
                case 1 -> selectQueries();
                case 2 -> insertQueries();
                case 3 -> updateQueries();
                case 4 -> deleteQueries();
                case 5 -> printChoices();
            }
        } while (true);

    }

    public static int getChoice() {
        int choice;
        try {
            choice = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("\nIncorrect input, try again");
            choice = 100;
        }
        return choice;
    }

    private static void selectQueries() {
        System.out.println("""
                --Show--
                1.Courses
                2.TBD
                3.TBD
                4.TBD
                5.TBD
                6.TBD
                \nAwaiting input
                """);
        int choice = getChoice();
        switch (choice) {
            default -> System.out.println("Incorrect choice, returning to menu");
            case 1 -> CourseQueries.selectCourseQueries();
            case 2 -> /*Todo: MenuOption2 for select*/{}
            case 3 -> /*Todo: MenuOption3 for select*/{}
            case 4 -> /*Todo: MenuOption4 for select*/{}
            case 5 -> /*Todo: MenuOption5 for select*/{}
            case 6 -> /*Todo: MenuOption6 for select*/{}
        }
    }
    private static void insertQueries() {
        System.out.println("""
                --Add--
                1.TBD
                2.TBD
                3.TBD
                4.TBD
                5.TBD
                6.TBD
                \nAwaiting input
                """);
        int choice = getChoice();
        switch (choice) {
            default -> System.out.println("Incorrect choice, returning to menu");
            case 1 -> /*Todo: MenuOption1 for insert*/{}
            case 2 -> /*Todo: MenuOption2 for insert*/{}
            case 3 -> /*Todo: MenuOption3 for insert*/{}
            case 4 -> /*Todo: MenuOption4 for insert*/{}
            case 5 -> /*Todo: MenuOption5 for insert*/{}
            case 6 -> /*Todo: MenuOption6 for insert*/{}
        }
    }

    private static void updateQueries() {
        System.out.println("""
                --Update--
                1.TBD
                2.TBD
                3.TBD
                4.TBD
                5.TBD
                6.TBD
                \nAwaiting input
                """);
        int choice = getChoice();
        switch (choice) {
            default -> System.out.println("Incorrect choice, returning to menu");
            case 1 -> /*Todo: MenuOption1 for update*/{}
            case 2 -> /*Todo: MenuOption2 for update*/{}
            case 3 -> /*Todo: MenuOption3 for update*/{}
            case 4 -> /*Todo: MenuOption4 for update*/{}
            case 5 -> /*Todo: MenuOption5 for update*/{}
            case 6 -> /*Todo: MenuOption6 for update*/{}
        }
    }

    private static void deleteQueries() {
        System.out.println("""
                --Delete--
                1.TBD
                2.TBD
                3.TBD
                4.TBD
                5.TBD
                6.TBD
                \nAwaiting input
                """);
        int choice = getChoice();
        switch (choice) {
            default -> System.out.println("Incorrect choice, returning to menu");
            case 1 -> /*Todo: MenuOption1 for delete*/{}
            case 2 -> /*Todo: MenuOption2 for delete*/{}
            case 3 -> /*Todo: MenuOption3 for delete*/{}
            case 4 -> /*Todo: MenuOption4 for delete*/{}
            case 5 -> /*Todo: MenuOption5 for delete*/{}
            case 6 -> /*Todo: MenuOption6 for delete*/{}
        }
    }

    private static void printChoices() {
        System.out.println("""
                0 - Exit program
                1 - Show
                2 - Add
                3 - Update
                4 - Delete
                5 - Print choices""");
    }

    public static void inTransaction(Consumer<EntityManager> work) {
        try (EntityManager entityManager = JPAUtil.getEntityManager()) {
            EntityTransaction transaction = entityManager.getTransaction();
            try {
                transaction.begin();
                work.accept(entityManager);
                transaction.commit();
            } catch (Exception e) {
                if (transaction.isActive()) {
                    transaction.rollback();
                }
                throw e;
            }
        }
    }
}
