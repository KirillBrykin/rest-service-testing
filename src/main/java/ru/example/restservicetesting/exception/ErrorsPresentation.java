package ru.example.restservicetesting.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ErrorsPresentation {
    private List<String> errors;
}
