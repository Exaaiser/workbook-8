package com.pluralsight;

import java.sql.*;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/northwind";
        String username = "root";
        String password = "umut1453";

        Connection connection = null;
        Scanner scanner = new Scanner(System.in);

        try {
            connection = DriverManager.getConnection(url, username, password);
            System.out.println("Connected!");

            int choice;

            do {
                System.out.println("\n--- Main Menu ---");
                System.out.println("1) List of all products ");
                System.out.println("2) List of all customers");
                System.out.println("0) Exit");
                System.out.print("Input: ");
                choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1:
                        displayProducts(connection);
                        break;
                    case 2:
                        displayCustomers(connection);
                        break;
                    case 0:
                        System.out.println("Exiting...");
                        break;
                    default:
                        System.out.println("Invalid option, please try again.");
                }

            } while (choice != 0);

        } catch (SQLException e) {
            System.out.println("Connection Error!");
            e.printStackTrace();
        } finally {
            try {
                if (connection != null)
                    connection.close();
                scanner.close();
            } catch (SQLException e) {
                System.out.println("Error 404");
                e.printStackTrace();
            }
        }
    }

    private static void displayProducts(Connection connection) {
        String query = "SELECT ProductName FROM products";

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            System.out.println("\n--- Northwind Items ---");
            while (resultSet.next()) {
                System.out.println(resultSet.getString("ProductName"));
            }

        } catch (SQLException e) {
            System.out.println("Error");
            e.printStackTrace();
        }
    }

    private static void displayCustomers(Connection connection) {
        String query = "SELECT ContactName, CompanyName, City, Country, Phone FROM customers ORDER BY Country";

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            System.out.println("\n---Customer Informations ---");
            while (resultSet.next()) {
                String name = resultSet.getString("ContactName");
                String company = resultSet.getString("CompanyName");
                String city = resultSet.getString("City");
                String country = resultSet.getString("Country");
                String phone = resultSet.getString("Phone");

                System.out.printf("Name: %s | Company: %s | Town: %s | Country: %s | Phone: %s\n",
                        name, company, city, country, phone);
            }

        } catch (SQLException e) {
            System.out.println("Error.");
            e.printStackTrace();
        }
    }
}
