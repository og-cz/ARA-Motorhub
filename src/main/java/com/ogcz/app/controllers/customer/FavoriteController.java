package com.ogcz.app.controllers.customer;

import java.io.IOException;
import java.net.URI;
import java.awt.Desktop;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.ogcz.app.SettingsWindowHelper;
import com.ogcz.app.database.DatabaseConnection;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

public class FavoriteController {

    @FXML
    private ComboBox<String> categoryComboBox;

    @FXML
    void initialize() {
        // populate ComboBox with categories
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement("SELECT name FROM category");
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                categoryComboBox.getItems().add(rs.getString("name"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        // handle category selection
        categoryComboBox.setOnAction(event -> {
            String selectedCategory = categoryComboBox.getValue();
            if (selectedCategory != null && !selectedCategory.isEmpty()) {
                openCategoriesPage(selectedCategory);
            }
        });
    }

    private void openCategoriesPage(String category) {
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

    @FXML
    void handleHomeButton(ActionEvent event) {
        System.out.println("handleHomeButton triggered");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/customer/HomeMain.fxml"));
            Parent newRoot = loader.load();

            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene newScene = new Scene(newRoot);
            currentStage.setScene(newScene);
            currentStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void handleLogOutButton(ActionEvent event) {
        System.out.println("Logout clicked");
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        SettingsWindowHelper.logout(currentStage);
    }

    @FXML
    private void handleOpenSettings(ActionEvent event) {
        SettingsWindowHelper.openSettings((Node) event.getSource());
    }

    @FXML
    void handleShopsClick(ActionEvent event) {
        System.out.println("handleShopsClick triggered");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/customer/Shops.fxml"));
            Parent newRoot = loader.load();

            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene newScene = new Scene(newRoot);
            currentStage.setScene(newScene);
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleProductsClick(ActionEvent event) {
        System.out.println("handleProductsClick triggered");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/customer/Products.fxml"));
            Parent newRoot = loader.load();

            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene newScene = new Scene(newRoot);
            currentStage.setScene(newScene);
            currentStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML 
    void handleCartClick(ActionEvent event) {
        System.out.println("handleCartClick triggered");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/customer/Cart.fxml"));
            Parent newRoot = loader.load();

            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene newScene = new Scene(newRoot);
            currentStage.setScene(newScene);
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }        
    }

    @FXML
    private void handleProfileClick(ActionEvent event) {
        System.out.println("handleProfileClick triggered");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/customer/Profile.fxml"));
            Parent newRoot = loader.load();

            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene newScene = new Scene(newRoot);
            currentStage.setScene(newScene);
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void handleOGCREATE(ActionEvent event) {
        try {
            URI uri = new URI("https://www.instagram.com/_ogcreate/");
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                Desktop.getDesktop().browse(uri);
            } else {
                System.out.println("Desktop browsing not supported.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
