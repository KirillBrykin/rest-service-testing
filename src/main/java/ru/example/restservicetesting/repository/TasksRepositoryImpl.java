package ru.example.restservicetesting.repository;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.example.restservicetesting.model.Task;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
@Getter
public class TasksRepositoryImpl implements TasksRepository {

    private final List<Task> tasks = new LinkedList<>();

    @Override
    public List<Task> findAll() {
        return this.tasks;
    }

    @Override
    public void save(Task task) {
        this.tasks.add(task);
    }

    @Override
    public Optional<Task> findById(UUID id) {
        return this.tasks.stream().filter(task -> task.getId().equals(id)).findFirst();
    }
}