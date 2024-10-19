package org.example;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.geometry.Insets;
import javafx.scene.layout.HBox;

public class EmployeeUI {

    private ObservableList<Employee> employeeList = FXCollections.observableArrayList();
    private TableView<Employee> employeeTable;
    private TextField nameField, ageField, positionField, salaryField;

    public GridPane createEmployeeForm() {
        GridPane formGrid = new GridPane();
        formGrid.setPadding(new Insets(10));
        formGrid.setHgap(10);
        formGrid.setVgap(10);

        Label nameLabel = new Label("Name:");
        nameField = new TextField();
        Label ageLabel = new Label("Age:");
        ageField = new TextField();
        Label positionLabel = new Label("Position:");
        positionField = new TextField();
        Label salaryLabel = new Label("Salary:");
        salaryField = new TextField();

        Button addButton = new Button("Add");
        addButton.setOnAction(e -> addEmployee());
        Button updateButton = new Button("Update");
        updateButton.setOnAction(e -> updateEmployee());
        Button deleteButton = new Button("Delete");
        deleteButton.setOnAction(e -> deleteEmployee());

        formGrid.add(nameLabel, 0, 0);
        formGrid.add(nameField, 1, 0);
        formGrid.add(ageLabel, 0, 1);
        formGrid.add(ageField, 1, 1);
        formGrid.add(positionLabel, 0, 2);
        formGrid.add(positionField, 1, 2);
        formGrid.add(salaryLabel, 0, 3);
        formGrid.add(salaryField, 1, 3);

        HBox buttonBox = new HBox(10, addButton, updateButton, deleteButton);
        formGrid.add(buttonBox, 1, 4);

        return formGrid;
    }

    public TableView<Employee> createEmployeeTable() {
        employeeTable = new TableView<>(employeeList);
        setupEmployeeTableColumns();
        employeeTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        employeeTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                populateFields(newSelection);
            }
        });
        return employeeTable;
    }

    private void setupEmployeeTableColumns() {
        TableColumn<Employee, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(data -> data.getValue().nameProperty());

        TableColumn<Employee, Integer> ageColumn = new TableColumn<>("Age");
        ageColumn.setCellValueFactory(data -> data.getValue().ageProperty().asObject());

        TableColumn<Employee, String> positionColumn = new TableColumn<>("Position");
        positionColumn.setCellValueFactory(data -> data.getValue().positionProperty());

        TableColumn<Employee, Double> salaryColumn = new TableColumn<>("Salary");
        salaryColumn.setCellValueFactory(data -> data.getValue().salaryProperty().asObject());

        employeeTable.getColumns().addAll(nameColumn, ageColumn, positionColumn, salaryColumn);
    }

    public void fetchEmployeeData() {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/employeeService/getEmployeeList"))
                .GET()
                .build();

        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(this::updateEmployeeList)
                .exceptionally(e -> {
                    showAlert("Error", "Failed to fetch employees: " + e.getMessage());
                    return null;
                });
    }

    private void addEmployee() {
        try {
            Employee newEmployee = new Employee(
                    nameField.getText(),
                    Integer.parseInt(ageField.getText()),
                    positionField.getText(),
                    Double.parseDouble(salaryField.getText())
            );

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/employeeService/addEmployee"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(new ObjectMapper().writeValueAsString(newEmployee)))
                    .build();

            client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenAccept(response -> {
                        fetchEmployeeData();  // Refresh the employee list after adding
                        clearFields();  // Clear the input fields
                    })
                    .exceptionally(e -> {
                        showAlert("Error", "Failed to add employee: " + e.getMessage());
                        return null;
                    });
        } catch (Exception e) {
            showAlert("Error", "Please enter valid employee details.");
        }
    }

    private void updateEmployee() {
        Employee selectedEmployee = employeeTable.getSelectionModel().getSelectedItem();
        if (selectedEmployee != null) {
            try {
                // Create a new Employee object with updated details
                Employee updatedEmployee = new Employee(
                        nameField.getText(),
                        Integer.parseInt(ageField.getText()),
                        positionField.getText(),
                        Double.parseDouble(salaryField.getText())
                );
                updatedEmployee.setId(selectedEmployee.getId()); // Set the ID of the selected employee

                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create("http://localhost:8080/employeeService/" + selectedEmployee.getId())) // Pass the ID here
                        .header("Content-Type", "application/json")
                        .PUT(HttpRequest.BodyPublishers.ofString(new ObjectMapper().writeValueAsString(updatedEmployee)))
                        .build();

                client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                        .thenAccept(response -> {
                            fetchEmployeeData();  // Refresh the employee list after updating
                            clearFields();  // Clear the input fields
                        })
                        .exceptionally(e -> {
                            showAlert("Error", "Failed to update employee: " + e.getMessage());
                            return null;
                        });
            } catch (Exception e) {
                showAlert("Error", "Please enter valid employee details.");
            }
        } else {
            showAlert("Error", "No employee selected for update.");
        }
    }


    private void deleteEmployee() {
        Employee selectedEmployee = employeeTable.getSelectionModel().getSelectedItem();
        if (selectedEmployee != null) {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/employeeService/" + selectedEmployee.getId()))
                    .DELETE()
                    .build();

            client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenAccept(response -> {
                        System.out.println("Response from delete: " + response); // Debug output
                        fetchEmployeeData();  // Refresh the employee list after deletion
                        clearFields();  // Clear the input fields
                    })
                    .exceptionally(e -> {
                        showAlert("Error", "Failed to delete employee: " + e.getMessage());
                        return null;
                    });
        } else {
            showAlert("Error", "No employee selected for deletion.");
        }
    }

    private void updateEmployeeList(String responseBody) {
        if (responseBody == null || responseBody.isEmpty()) {
            showAlert("Error", "No employee data received.");
            return;
        }

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            ApiResponse apiResponse = objectMapper.readValue(responseBody, ApiResponse.class);
            List<Employee> employees = apiResponse.getData();

            employeeList.clear();
            employeeList.addAll(employees);
        } catch (Exception e) {
            showAlert("Error", "Failed to parse employee data: " + e.getMessage());
        }
    }

    private void populateFields(Employee employee) {
        nameField.setText(employee.getName());
        ageField.setText(String.valueOf(employee.getAge()));
        positionField.setText(employee.getPosition());
        salaryField.setText(String.valueOf(employee.getSalary()));
    }

    private void clearFields() {
        nameField.clear();
        ageField.clear();
        positionField.clear();
        salaryField.clear();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
