package com.ogcz.app.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class CartService {

    public static Integer fetchOrCreateCartForCurrentUser() {
        int userId = UserSession.getCurrentUser().getUserId();

        try (Connection conn = DatabaseConnection.connect()) {
            // 1. check for existing cart
            String checkSql = "SELECT id FROM carts WHERE customer_id = ? AND checked_out = 0";
            try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                checkStmt.setInt(1, userId);
                ResultSet rs = checkStmt.executeQuery();
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }

            // 2. create new cart
            String insertSql = "INSERT INTO carts (customer_id, checked_out) VALUES (?, 0)";
            try (PreparedStatement insertStmt = conn.prepareStatement(insertSql, PreparedStatement.RETURN_GENERATED_KEYS)) {
                insertStmt.setInt(1, userId);
                insertStmt.executeUpdate();

                ResultSet keys = insertStmt.getGeneratedKeys();
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
