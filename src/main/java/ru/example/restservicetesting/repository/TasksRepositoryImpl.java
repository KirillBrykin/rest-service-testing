package ru.example.restservicetesting.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.example.restservicetesting.dao.TaskEntityDAO;
import ru.example.restservicetesting.mapper.TaskMapper;
import ru.example.restservicetesting.model.Task;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class TasksRepositoryImpl implements TasksRepository {

    private final TaskEntityDAO taskEntityDAO;
    private final TaskMapper taskMapper;

    @Override
    public List<Task> findAll() {
        return taskMapper.toDto(taskEntityDAO.findAll());
    }

    @Override
    public void save(Task task) {
        taskEntityDAO.save(taskMapper.toEntity(task));
    }

    @Override
    public Optional<Task> findById(UUID id) {
        return taskEntityDAO.findById(id).map(taskMapper::toDto);
    }
}