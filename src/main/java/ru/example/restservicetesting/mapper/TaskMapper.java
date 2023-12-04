package ru.example.restservicetesting.mapper;

import org.mapstruct.Mapper;
import ru.example.restservicetesting.dao.entity.TaskEntity;
import ru.example.restservicetesting.model.Task;

@Mapper(componentModel = "spring")
public interface TaskMapper extends AbstractEntityMapper<Task, TaskEntity> {
}