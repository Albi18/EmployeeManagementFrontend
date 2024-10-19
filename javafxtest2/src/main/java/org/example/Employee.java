package org.example;


import javafx.beans.property.*;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Employee {
    private SimpleIntegerProperty id;
    private SimpleStringProperty name;
    private SimpleIntegerProperty age;
    private SimpleStringProperty position;
    private SimpleDoubleProperty salary;

    // Default constructor
    public Employee() {
        this.id = new SimpleIntegerProperty();
        this.name = new SimpleStringProperty();
        this.age = new SimpleIntegerProperty();
        this.position = new SimpleStringProperty();
        this.salary = new SimpleDoubleProperty();
    }

    // Constructor with parameters
    public Employee(int id, String name, int age, String position, double salary) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.age = new SimpleIntegerProperty(age);
        this.position = new SimpleStringProperty(position);
        this.salary = new SimpleDoubleProperty(salary);
    }

    // Overloaded constructor without ID
    public Employee(String name, int age, String position, double salary) {
        this.name = new SimpleStringProperty(name);
        this.age = new SimpleIntegerProperty(age);
        this.position = new SimpleStringProperty(position);
        this.salary = new SimpleDoubleProperty(salary);
        this.id = new SimpleIntegerProperty(0); // Or set this to a default value
    }

    // Getter and Setter methods
    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public SimpleIntegerProperty idProperty() {
        return id;
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public int getAge() {
        return age.get();
    }

    public void setAge(int age) {
        this.age.set(age);
    }

    public SimpleIntegerProperty ageProperty() {
        return age;
    }

    public String getPosition() {
        return position.get();
    }

    public void setPosition(String position) {
        this.position.set(position);
    }

    public SimpleStringProperty positionProperty() {
        return position;
    }

    public double getSalary() {
        return salary.get();
    }

    public void setSalary(double salary) {
        this.salary.set(salary);
    }

    public SimpleDoubleProperty salaryProperty() {
        return salary;
    }
}

