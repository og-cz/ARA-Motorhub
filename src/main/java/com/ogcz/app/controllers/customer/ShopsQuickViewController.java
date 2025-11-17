package com.ogcz.app.controllers.customer;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

import com.ogcz.app.SettingsWindowHelper;
import com.ogcz.app.database.DatabaseConnection;
import com.ogcz.app.database.Shops;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class ShopsQuickViewController implements Initializable {

    @FXML
    private Label labelCategory1, labelCategory2, labelCategory3, labelCategory4;
    @FXML
    private Label labelCategory5, labelCategory6, labelCategory7, labelCategory8;
    @FXML
    private Label labelCategoryItem1, labelCategoryItem2, labelCategoryItem3, labelCategoryItem4;
    @FXML
    private Label labelCategoryItem5, labelCategoryItem6, labelCategoryItem7, labelCategoryItem8;
    @FXML
    private Label sellerAddress, sellerEmail, sellerName, sellerBarangay;

    private Shops shop;

    @FXML
    private ComboBox<String> categoryComboBox;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        categoryComboBox.setPromptText("Category");

        try (Connection conn = DatabaseConnection.connect();
                PreparedStatement stmt = conn.prepareStatement("SELECT name FROM category");
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                categoryComboBox.getItems().add(rs.getString("name"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        categoryComboBox.setOnAction(event -> {
            String selectedCategory = categoryComboBox.getValue();
            if (selectedCategory != null && !selectedCategory.isEmpty()) {
                openCategoriesPage(selectedCategory);
            }
        });
    }

    private void openCategoriesPage(String category) {
        System.out.println("Opening category: " + category);
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/customer/Categories.fxml"));
            Parent newRoot = loader.load();

            CategoriesController controller = loader.getController();
            controller.setSelectedCategory(category);

            Stage currentStage = (Stage) categoryComboBox.getScene().getWindow();
            currentStage.setScene(new Scene(newRoot));
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void setShopDetails(Shops shop) {

        if (shop == null) {
            System.err.println("ERROR: Shop passed to setShopDetails is null!");
            return;
        }
        System.out.println("Received shop ID: " + shop.getShopId());

        this.shop = shop;

        try (Connection conn = DatabaseConnection.connect();
                PreparedStatement stmt = conn.prepareStatement(
                        "SELECT first_name, last_name FROM user WHERE user_id = ?")) {

            stmt.setInt(1, shop.getShopId());
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String fullName = rs.getString("first_name") + " " + rs.getString("last_name");
                sellerName.setText(fullName);
            } else {
                sellerName.setText("Unknown Seller");
            }

        } catch (Exception e) {
            e.printStackTrace();
            sellerName.setText("Error");
        }

        sellerEmail.setText(shop.getShopEmail() != null ? shop.getShopEmail() : "N/A");
        sellerAddress.setText(shop.getShopAddress() != null ? shop.getShopAddress() : "N/A");
        sellerBarangay.setText(shop.getShopBarangay() != null ? shop.getShopBarangay() : "N/A");

        if (shop.getShopId() > 0) {
            loadCategoryData(shop.getShopId());
        }
    }

    private void navigateToCategory(String categoryName, ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/fxml/customer/ShopsQuickViewCategory.fxml"));
            Parent newRoot = loader.load();

            ShopsQuickViewCategoryController controller = loader.getController();
            controller.setShopDetails(shop);
            controller.filterByCategory(categoryName, sellerName);

            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.setScene(new Scene(newRoot));
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void handleCategoryBolts(ActionEvent event) {
        navigateToCategory("Bolts", event);
    }

    @FXML
    void handleCategoryElectrical(ActionEvent event) {
        navigateToCategory("Electrical", event);
    }

    @FXML
    void handleCategoryEngine(ActionEvent event) {
        navigateToCategory("Engine", event);
    }

    @FXML
    void handleCategoryExterior(ActionEvent event) {
        navigateToCategory("Exterior", event);
    }

    @FXML
    void handleCategoryOil(ActionEvent event) {
        navigateToCategory("Oil", event);
    }

    @FXML
    void handleCategorySuspension(ActionEvent event) {
        navigateToCategory("Suspension", event);
    }

    @FXML
    void handleCategoryTransmission(ActionEvent event) {
        navigateToCategory("Transmission", event);
    }

    @FXML
    void handleCategoryWheels(ActionEvent event) {
        navigateToCategory("Wheels", event);
    }

    private void loadCategoryData(int sellerId) {
        String query = """
                    SELECT c.name AS category_name, COUNT(p.product_id) AS product_count
                    FROM category c
                    JOIN product p ON c.category_id = p.category_id
                    WHERE p.seller_id = ? AND p.status = 'AVAILABLE'
                    GROUP BY c.category_id, c.name
                    ORDER BY c.category_id ASC
                """;

        try (Connection conn = DatabaseConnection.connect();
                PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, sellerId);
            ResultSet rs = stmt.executeQuery();

            int index = 1;
            while (rs.next() && index <= 8) {
                String category = rs.getString("category_name");
                int count = rs.getInt("product_count");

                switch (index) {
                    case 1 -> {
                        labelCategory1.setText(category);
                        labelCategoryItem1.setText(count + " items");
                    }
                    case 2 -> {
                        labelCategory2.setText(category);
                        labelCategoryItem2.setText(count + " items");
                    }
                    case 3 -> {
                        labelCategory3.setText(category);
                        labelCategoryItem3.setText(count + " items");
                    }
                    case 4 -> {
                        labelCategory4.setText(category);
                        labelCategoryItem4.setText(count + " items");
                    }
                    case 5 -> {
                        labelCategory5.setText(category);
                        labelCategoryItem5.setText(count + " items");
                    }
                    case 6 -> {
                        labelCategory6.setText(category);
                        labelCategoryItem6.setText(count + " items");
                    }
                    case 7 -> {
                        labelCategory7.setText(category);
                        labelCategoryItem7.setText(count + " items");
                    }
                    case 8 -> {
                        labelCategory8.setText(category);
                        labelCategoryItem8.setText(count + " items");
                    }
                }
                index++;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // NAVIGATION HANDLERS
    @FXML
    void handleHomeButton(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/customer/HomeMain.fxml"));
            Parent newRoot = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(newRoot));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void handleProductsClick(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/customer/Products.fxml"));
            Parent newRoot = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(newRoot));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void handleShopsClick(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/customer/Shops.fxml"));
            Parent newRoot = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(newRoot));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void handleCartClick(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/customer/Cart.fxml"));
            Parent newRoot = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(newRoot));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void handleFavoriteClick(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/customer/Favorite.fxml"));
            Parent newRoot = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(newRoot));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void handleOpenSettings(ActionEvent event) {
        SettingsWindowHelper.openSettings((Node) event.getSource());
    }

    @FXML
    void handleLogOutButton(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        SettingsWindowHelper.logout(stage);
    }
}
