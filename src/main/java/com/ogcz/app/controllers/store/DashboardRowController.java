package com.ogcz.app.controllers.store;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.ogcz.app.database.DatabaseConnection;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class DashboardRowController {

    @FXML
    private Label labelAction;

    @FXML
    private Label labelStatus;

    @FXML
    private Label labelAddress;

    @FXML
    private Label labelDate;

    @FXML
    private Label labelName;

    @FXML
    private Label labelPrice;

    @FXML
    private Label labelQuantity;

    @FXML
    private Label labelUserID;

        private int orderId;

        public void setDashboardController(DashboardController controller) {
    }
    public void setData(String name, String address, String date, String quantity, String price, String status,
            int orderId) {
        labelName.setText(name);
        labelAddress.setText(address);
        labelDate.setText(date);
        labelQuantity.setText(quantity);
        labelPrice.setText(price);
        labelStatus.setText(status);
        labelUserID.setText("" + orderId);
        this.orderId = orderId;

        switch (status.toUpperCase()) {
            case "COMPLETED":
                labelStatus.setStyle("-fx-text-fill: #00C755;");
                break;
            case "CANCELLED":
                labelStatus.setStyle("-fx-text-fill: #C70C00;");
                break;
            case "PENDING":
                labelStatus.setStyle("-fx-text-fill: #D59700;");
                break;
        }
    }

    @FXML
    void handleDeleteOrder(ActionEvent event) {

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("ARA Motorhub");
        confirm.setHeaderText("Delete Order");
        confirm.setContentText("Are you sure you want to delete this order?");
        Stage stage = (Stage) confirm.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(this.getClass().getResource("/assets/z_favicon.png").toString()));

        confirm.showAndWait().ifPresent(response -> {
            if (response.getButtonData().isDefaultButton()) {
                deleteOrderFromDatabase();
                removeOrderFromUI();

                Alert success = new Alert(Alert.AlertType.INFORMATION);
                success.setTitle("Order Deleted");
                success.setHeaderText(null);
                success.setContentText("The order has been successfully deleted.");
                success.showAndWait();
            }
        });
    }

    private void deleteOrderFromDatabase() {
        String deleteOrderItemsSQL = "DELETE FROM order_item WHERE order_id = ?";
        String deleteOrderSQL = "DELETE FROM `order` WHERE order_id = ?";

        try (Connection conn = DatabaseConnection.connect()) {
            try (PreparedStatement stmt1 = conn.prepareStatement(deleteOrderItemsSQL);
                    PreparedStatement stmt2 = conn.prepareStatement(deleteOrderSQL)) {

                stmt1.setInt(1, orderId);
                stmt1.executeUpdate();

                stmt2.setInt(1, orderId);
                stmt2.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void removeOrderFromUI() {
        Parent thisRow = labelName.getParent().getParent(); 
        ((VBox) thisRow.getParent()).getChildren().remove(thisRow);
    }


    private void updateOrderStatus(String newStatus) {

        switch (newStatus.toUpperCase()) {
            case "COMPLETED":
                labelStatus.setStyle("-fx-text-fill: #00C755;");
                break;
            case "PENDING":
                labelStatus.setStyle("-fx-text-fill: #C70C00;");
                break;
            case "CANCELLED":
                labelStatus.setStyle("-fx-text-fill: #D59700;");
                break;
        }

        String sql = "UPDATE `order` SET status = ? WHERE order_id = ?";
        try (Connection conn = DatabaseConnection.connect();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newStatus);
            stmt.setInt(2, orderId);
            stmt.executeUpdate();
            labelStatus.setText(newStatus); // reflect change in UI
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void handleSetCancelled(ActionEvent event) {
        updateOrderStatus("CANCELLED");
    }

    @FXML
    void handleSetCompleted(ActionEvent event) {
        updateOrderStatus("COMPLETED");
    }

    @FXML
    void handleSetPending(ActionEvent event) {
        updateOrderStatus("PENDING");
    }

}
