package ru.example.restservicetesting.model;

import lombok.Data;

import java.util.UUID;

@Data
public class Task {
    private UUID id;
    private String details;
    private boolean completed;

    public Task(String details) {
        this.id = UUID.randomUUID();
        this.details = details;
        this.completed = false;
    }
}
