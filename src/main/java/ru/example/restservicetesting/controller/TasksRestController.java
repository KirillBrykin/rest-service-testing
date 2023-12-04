package ru.example.restservicetesting.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import ru.example.restservicetesting.exception.ErrorsPresentation;
import ru.example.restservicetesting.model.NewTaskPayload;
import ru.example.restservicetesting.model.Task;
import ru.example.restservicetesting.repository.TasksRepository;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/task")
public class TasksRestController {

    private final TasksRepository taskRepository;
    private final MessageSource messageSource;

    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks() {
        System.out.println("TasksRestController.getAllTasks");
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(this.taskRepository.findAll());
    }

    @PostMapping
    public ResponseEntity<?> createNewTask(
            @RequestBody NewTaskPayload payload,
            UriComponentsBuilder uriComponentsBuilder,
            Locale locale) {
        System.out.println("TasksRestController.createNewTask");
        if (payload.getDetails() == null || payload.getDetails().isBlank()) {
            final String message = this.messageSource
                    .getMessage("tasks.create.details.errors.not_set",
                            new Object[0], locale);
            return ResponseEntity.badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new ErrorsPresentation(List.of(message)));
        } else {
            var task = new Task(payload.getDetails());
            this.taskRepository.save(task);
            return ResponseEntity.created(uriComponentsBuilder
                            .path("/api/tasks/{taskId}")
                            .build(Map.of("taskId", task.getId())))
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(task);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> findTaskById(@PathVariable("id") UUID id) {
        System.out.println("TasksRestController.findTaskById");
        return ResponseEntity.of(this.taskRepository.findById(id));
    }
}