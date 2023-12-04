package ru.example.restservicetesting.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
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
