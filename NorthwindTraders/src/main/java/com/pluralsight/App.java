package com.pluralsight;

import java.sql.*;

public class App {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/northwind";
        String username = "root";
        String password = "123-123-123";
        try {

            Connection connection = DriverManager.getConnection(url, username, password);
            System.out.println("Veritabanına başarıyla bağlanıldı!");


            String query = "SELECT ProductName FROM products";


            Statement statement = connection.createStatement();


            ResultSet resultSet = statement.executeQuery(query);

            System.out.println("\n--- Northwind Ürünleri ---");
            while (resultSet.next()) {
                String productName = resultSet.getString("ProductName");
                System.out.println(productName);
            }

            // 6. Kaynakları serbest bırak
            resultSet.close();
            statement.close();
            connection.close();

        } catch (SQLException e) {
            System.out.println("Veritabanı bağlantı hatası!");
            e.printStackTrace();
        }
    }
}
