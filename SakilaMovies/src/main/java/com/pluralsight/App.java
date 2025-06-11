package com.pluralsight;

import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.*;
import java.util.Scanner;

public class App {
    private static BasicDataSource dataSource;

    public static void main(String[] args) {
        setupDataSource();

        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("Enter an actor's last name: ");
            String lastName = scanner.nextLine();

            findActorsByLastName(lastName);

            System.out.print("\nEnter the actor's full name (First Last): ");
            String[] fullName = scanner.nextLine().split(" ");
            if (fullName.length < 2) {
                System.out.println("Invalid input. Please enter both first and last names.");
                return;
            }
            String firstName = fullName[0];
            String lastNameFull = fullName[1];

            findMoviesByActor(firstName, lastNameFull);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void setupDataSource() {
        dataSource = new BasicDataSource();
        dataSource.setUrl("jdbc:mysql://localhost:3306/Sakila");
        dataSource.setUsername("root");
        dataSource.setPassword("umut1453");
    }

    private static void findActorsByLastName(String lastName) {
        String sql = "SELECT actor_id, first_name, last_name FROM actor WHERE last_name = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, lastName.toUpperCase());

            try (ResultSet rs = stmt.executeQuery()) {
                boolean found = false;
                System.out.println("\n--- Actors Found ---");
                while (rs.next()) {
                    found = true;
                    System.out.printf("ID: %d | %s %s\n",
                            rs.getInt("actor_id"),
                            rs.getString("first_name"),
                            rs.getString("last_name"));
                }

                if (!found) {
                    System.out.println("No actors found with that last name.");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void findMoviesByActor(String firstName, String lastName) {
        String sql = """
            SELECT f.title
            FROM film f
            JOIN film_actor fa ON f.film_id = fa.film_id
            JOIN actor a ON fa.actor_id = a.actor_id
            WHERE a.first_name = ? AND a.last_name = ?
            """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, firstName.toUpperCase());
            stmt.setString(2, lastName.toUpperCase());

            try (ResultSet rs = stmt.executeQuery()) {
                boolean found = false;
                System.out.println("\n--- Movies Featuring the Actor ---");
                while (rs.next()) {
                    found = true;
                    System.out.println("• " + rs.getString("title"));
                }

                if (!found) {
                    System.out.println("No movies found for this actor.");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}