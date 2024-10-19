package org.example;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class ApiResponse {

    private String message;

    @JsonProperty("data")
    private List<Employee> data;

    // Getters and setters
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Employee> getData() {
        return data;
    }

    public void setData(List<Employee> data) {
        this.data = data;
    }
}
