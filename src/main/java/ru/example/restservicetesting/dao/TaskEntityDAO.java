package ru.example.restservicetesting.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.example.restservicetesting.dao.entity.TaskEntity;
import java.util.UUID;

public interface TaskEntityDAO extends JpaRepository<TaskEntity, UUID> {
}