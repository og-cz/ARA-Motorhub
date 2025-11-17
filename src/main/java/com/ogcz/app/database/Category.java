package com.ogcz.app.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Category {
    private int id;
    private String name;

    public Category(int id, String name) {
        this.id = id;
        this.name = name;
    }

    // getters
    public int getId() { return id; }
    public String getName() { return name; }

    // setters
    public void setId(int id) { this.id = id; }
    public void setName(String name) { this.name = name; }

    @Override
    public String toString() {
        return name; // so ComboBox displays category name
    }

    //fetch all categories from database
    public static List<Category> getAllCategories() {
        List<Category> categories = new ArrayList<>();

        String sql = "SELECT category_id, name FROM category";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("category_id");
                String name = rs.getString("name");
                categories.add(new Category(id, name));
            }

        } catch (SQLException e) {
            e.printStackTrace(); 
        }

        return categories;
    }
}
