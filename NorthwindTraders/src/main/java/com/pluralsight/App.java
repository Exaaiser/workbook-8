package com.pluralsight;

import java.sql.*;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/northwind";
        String username = "root";
        String password = "umut1453";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             Scanner scanner = new Scanner(System.in)) {

            System.out.println("Connected to the database successfully!");

            int choice;

            do {
                System.out.println("\n--- Main Menu ---");
                System.out.println("1) Display all products");
                System.out.println("2) Display all customers");
                System.out.println("3) Display all categories");
                System.out.println("0) Exit");
                System.out.print("Your choice: ");
                choice = scanner.nextInt();
                scanner.nextLine(); // clear newline

                switch (choice) {
                    case 1:
                        displayProducts(connection);
                        break;
                    case 2:
                        displayCustomers(connection);
                        break;
                    case 3:
                        displayCategoriesAndProducts(connection, scanner);
                        break;
                    case 0:
                        System.out.println("Exiting the program...");
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }

            } while (choice != 0);

        } catch (SQLException e) {
            System.out.println("Database connection error!");
            e.printStackTrace();
        }
    }

    private static void displayProducts(Connection connection) {
        String query = "SELECT ProductName FROM products";

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            System.out.println("\n--- Northwind Products ---");
            while (resultSet.next()) {
                System.out.println(resultSet.getString("ProductName"));
            }

        } catch (SQLException e) {
            System.out.println("Error retrieving products.");
            e.printStackTrace();
        }
    }

    private static void displayCustomers(Connection connection) {
        String query = "SELECT ContactName, CompanyName, City, Country, Phone FROM customers ORDER BY Country";

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            System.out.println("\n--- Customer Information ---");
            while (resultSet.next()) {
                String name = resultSet.getString("ContactName");
                String company = resultSet.getString("CompanyName");
                String city = resultSet.getString("City");
                String country = resultSet.getString("Country");
                String phone = resultSet.getString("Phone");

                System.out.printf("Name: %s | Company: %s | City: %s | Country: %s | Phone: %s\n",
                        name, company, city, country, phone);
            }

        } catch (SQLException e) {
            System.out.println("Error retrieving customer data.");
            e.printStackTrace();
        }
    }

    private static void displayCategoriesAndProducts(Connection connection, Scanner scanner) {
        String categoryQuery = "SELECT CategoryID, CategoryName FROM categories ORDER BY CategoryID";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(categoryQuery)) {

            System.out.println("\n--- Categories ---");
            while (rs.next()) {
                int categoryId = rs.getInt("CategoryID");
                String categoryName = rs.getString("CategoryName");
                System.out.printf("%d - %s\n", categoryId, categoryName);
            }

            System.out.print("\nEnter the Category ID to view its products: ");
            int selectedCategoryId = scanner.nextInt();
            scanner.nextLine(); // clear newline

            String productsQuery = "SELECT ProductID, ProductName, UnitPrice, UnitsInStock FROM products WHERE CategoryID = ?";

            try (PreparedStatement pstmt = connection.prepareStatement(productsQuery)) {
                pstmt.setInt(1, selectedCategoryId);

                try (ResultSet productRs = pstmt.executeQuery()) {
                    System.out.println("\n--- Products in Selected Category ---");
                    boolean hasProducts = false;
                    while (productRs.next()) {
                        hasProducts = true;
                        int productId = productRs.getInt("ProductID");
                        String productName = productRs.getString("ProductName");
                        double unitPrice = productRs.getDouble("UnitPrice");
                        int unitsInStock = productRs.getInt("UnitsInStock");

                        System.out.printf("ID: %d | Name: %s | Price: %.2f | Stock: %d\n",
                                productId, productName, unitPrice, unitsInStock);
                    }
                    if (!hasProducts) {
                        System.out.println("There are no products in this category.");
                    }
                }
            }

        } catch (SQLException e) {
            System.out.println("Error retrieving categories or products.");
            e.printStackTrace();
        }
    }
}
