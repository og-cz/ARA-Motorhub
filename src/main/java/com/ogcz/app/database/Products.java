package com.ogcz.app.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Products {

    private int productId;
    private String productName;
    private String productPrice;
    private int stock;
    private int categoryId;
    private String categoryName;
    private int sellerId;
    private String storeName;
    private String status;
    private Timestamp createdAt;
    private String description;

    private String storeEmail;
    private String storeAddress;
    private String storeBarangay;

    // constructor used in getProductsBySellerIdAndCategory
    public Products(int productId, int sellerId, int categoryId, String productName,
            String description, double price, int stock, String status, Timestamp createdAt) {
        this.productId = productId;
        this.sellerId = sellerId;
        this.categoryId = categoryId;
        this.productName = productName;
        this.description = description;
        this.productPrice = String.valueOf(price);
        this.stock = stock;
        this.status = status;
        this.createdAt = createdAt;
    }

    public Products() {
    }

    // getters
    public int getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public int getStock() {
        return stock;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public int getSellerId() {
        return sellerId;
    }

    public String getStoreName() {
        return storeName;
    }

    public String getStatus() {
        return status;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public String getStoreEmail() {
        return storeEmail;
    }

    public String getStoreAddress() {
        return storeAddress;
    }

    public String getStoreBarangay() {
        return storeBarangay;
    }

    public String getDescription() {
        return description;
    }

    // Setters
    public void setProductId(int productId) {
        this.productId = productId;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public void setSellerId(int sellerId) {
        this.sellerId = sellerId;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public void setStoreEmail(String storeEmail) {
        this.storeEmail = storeEmail;
    }

    public void setStoreAddress(String storeAddress) {
        this.storeAddress = storeAddress;
    }

    public void setStoreBarangay(String storeBarangay) {
        this.storeBarangay = storeBarangay;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // Static Data Access Methods
    public static List<Products> getProductsBySellerIdAndCategory(int sellerId, String categoryName) {
        List<Products> products = new ArrayList<>();
        String sql = """
                    SELECT p.*
                    FROM product p
                    JOIN category c ON p.category_id = c.category_id
                    WHERE p.seller_id = ? AND c.name = ?
                """;

        try (Connection conn = DatabaseConnection.connect();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, sellerId);
            stmt.setString(2, categoryName);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Products product = new Products(
                        rs.getInt("product_id"),
                        rs.getInt("seller_id"),
                        rs.getInt("category_id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getDouble("price"),
                        rs.getInt("stock"),
                        rs.getString("status"),
                        rs.getTimestamp("created_at"));
                products.add(product);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return products;
    }

    public static List<Products> getProductsBySellerAndCategory(int sellerId, int categoryId) {
        List<Products> productList = new ArrayList<>();
        String sql = "SELECT * FROM product WHERE seller_id = ? AND category_id = ? AND status = 'AVAILABLE'";

        try (Connection conn = DatabaseConnection.connect();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, sellerId);
            stmt.setInt(2, categoryId);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Products product = new Products();
                product.setProductId(rs.getInt("product_id"));
                product.setProductName(rs.getString("name"));
                product.setDescription(rs.getString("description"));
                product.setProductPrice(String.valueOf(rs.getDouble("price")));
                product.setStock(rs.getInt("stock"));
                product.setStatus(rs.getString("status"));
                product.setCategoryId(rs.getInt("category_id"));
                product.setSellerId(rs.getInt("seller_id"));
                productList.add(product);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return productList;
    }

    public static List<Products> getProductsBySellerId(int sellerId) {
        List<Products> productList = new ArrayList<>();
        String sql = """
                    SELECT
                        p.product_id,
                        p.name AS product_name,
                        p.price,
                        p.stock,
                        p.status,
                        p.category_id,
                        u.first_name,
                        u.last_name
                    FROM product p
                    JOIN user u ON p.seller_id = u.user_id
                    WHERE p.seller_id = ?
                    ORDER BY p.created_at DESC
                """;

        try (Connection conn = DatabaseConnection.connect();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, sellerId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Products p = new Products();
                p.setProductId(rs.getInt("product_id"));
                p.setProductName(rs.getString("product_name"));
                p.setProductPrice(rs.getString("price"));
                p.setStock(rs.getInt("stock"));
                p.setStatus(rs.getString("status"));
                p.setCategoryId(rs.getInt("category_id"));
                p.setSellerId(sellerId);

                String sellerName = rs.getString("first_name") + " " + rs.getString("last_name");
                p.setStoreName(sellerName.trim());

                productList.add(p);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return productList;
    }

    public static List<Products> getProductsByCategory(String categoryName) {
        List<Products> productsList = new ArrayList<>();
        String sql = """
                    SELECT
                        p.product_id,
                        p.name AS product_name,
                        p.price,
                        p.stock,
                        p.status,
                        p.created_at,
                        p.description,
                        p.category_id,
                        c.name AS category_name,
                        u.user_id AS seller_id,
                        u.first_name,
                        u.last_name
                    FROM product p
                    JOIN category c ON p.category_id = c.category_id
                    JOIN user u ON p.seller_id = u.user_id
                    WHERE c.name = ? AND p.status = 'AVAILABLE'
                """;

        try (Connection conn = DatabaseConnection.connect();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, categoryName);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Products p = new Products();
                p.setProductId(rs.getInt("product_id"));
                p.setProductName(rs.getString("product_name"));
                p.setProductPrice(String.valueOf(rs.getDouble("price")));
                p.setStock(rs.getInt("stock"));
                p.setStatus(rs.getString("status"));
                p.setCreatedAt(rs.getTimestamp("created_at"));
                p.setDescription(rs.getString("description"));
                p.setCategoryId(rs.getInt("category_id"));
                p.setCategoryName(rs.getString("category_name"));
                p.setSellerId(rs.getInt("seller_id"));

                String sellerName = rs.getString("first_name") + " " + rs.getString("last_name");
                p.setStoreName(sellerName.trim());

                productsList.add(p);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return productsList;
    }

    public static List<Products> getProductsByCategory(String category, int limit) {
        List<Products> list = new ArrayList<>();
        String sql = """
                    SELECT
                        p.product_id,
                        p.name AS product_name,
                        p.price,
                        u.first_name,
                        u.last_name
                    FROM product p
                    JOIN user u ON p.seller_id = u.user_id
                    JOIN category c ON p.category_id = c.category_id
                    WHERE c.name = ?
                    ORDER BY p.name ASC
                    LIMIT ?
                """;

        try (Connection conn = DatabaseConnection.connect();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, category);
            stmt.setInt(2, limit);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Products p = new Products();
                p.setProductId(rs.getInt("product_id"));
                p.setProductName(rs.getString("product_name"));
                p.setProductPrice(rs.getString("price"));

                String storeName = rs.getString("first_name") + " " + rs.getString("last_name");
                p.setStoreName(storeName);
                list.add(p);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public static List<Products> getAllAvailableProducts() {
        List<Products> productList = new ArrayList<>();
        String sql = """
                    SELECT
                        p.product_id,
                        p.name AS product_name,
                        p.price,
                        p.seller_id,
                        p.category_id,
                        p.stock,
                        p.status,
                        p.created_at,
                        u.first_name,
                        u.last_name
                    FROM product p
                    JOIN user u ON p.seller_id = u.user_id
                    WHERE p.status = 'AVAILABLE'
                    ORDER BY p.name ASC
                """;

        try (Connection conn = DatabaseConnection.connect();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Products p = new Products();
                p.setProductId(rs.getInt("product_id"));
                p.setProductName(rs.getString("product_name"));
                p.setProductPrice(rs.getString("price"));
                p.setSellerId(rs.getInt("seller_id"));
                p.setCategoryId(rs.getInt("category_id"));
                p.setStock(rs.getInt("stock"));
                p.setStatus(rs.getString("status"));
                p.setCreatedAt(rs.getTimestamp("created_at"));

                String sellerName = rs.getString("first_name") + " " + rs.getString("last_name");
                p.setStoreName(sellerName.trim());

                productList.add(p);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return productList;
    }
}
