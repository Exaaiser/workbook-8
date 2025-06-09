package com.pluralsight;

import java.sql.*;

public class App {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/northwind";
        String username = "root"; //
        String password = "umut1453"; //

        try (Connection connection = DriverManager.getConnection(url, username, password);
             Statement statement = connection.createStatement()) {


            String query = "SELECT ProductID, ProductName, UnitPrice, UnitsInStock FROM products";
            ResultSet resultSet = statement.executeQuery(query);


            String format = "%5s %35s %10s %5s%n";
            System.out.printf(format, "Id", "Name", "Price", "Stock");

            while (resultSet.next()) {
                int id = resultSet.getInt("ProductID");
                String name = resultSet.getString("ProductName");
                double price = resultSet.getDouble("UnitPrice");
                int stock = resultSet.getInt("UnitsInStock");

                System.out.printf("%5d %35s %10.2f %5d%n", id, name, price, stock);
            }
            resultSet.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}