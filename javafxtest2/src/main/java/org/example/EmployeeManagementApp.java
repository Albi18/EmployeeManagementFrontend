package org.example;

import javafx.application.Application;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import javafx.scene.layout.BorderPane;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class EmployeeManagementApp extends Application {
    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();
        Scene scene = new Scene(root, 800, 600);

        // Set the scene and title
        primaryStage.setTitle("Employee Management System");
        primaryStage.setScene(scene);
        primaryStage.show();

        // Initialize your UI components
        EmployeeUI employeeUI = new EmployeeUI();
        root.setCenter(employeeUI.createEmployeeForm());
        root.setBottom(employeeUI.createEmployeeTable());

        // Fetch employee data and update the table when the application launches
        employeeUI.fetchEmployeeData();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
