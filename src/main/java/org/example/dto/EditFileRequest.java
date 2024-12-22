package org.example.dto;

public class EditFileRequest {
    private String name;

    public EditFileRequest() {
    }

    public EditFileRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
