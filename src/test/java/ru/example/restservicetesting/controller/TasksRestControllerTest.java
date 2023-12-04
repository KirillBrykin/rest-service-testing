package ru.example.restservicetesting.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;
import ru.example.restservicetesting.exception.ErrorsPresentation;
import ru.example.restservicetesting.model.NewTaskPayload;
import ru.example.restservicetesting.model.Task;
import ru.example.restservicetesting.repository.TasksRepository;

import java.net.URI;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

//модульные тесты
@ExtendWith(MockitoExtension.class)
class TasksRestControllerTest {

    @Mock
    TasksRepository tasksRepository;

    @Mock
    MessageSource messageSource;

    @InjectMocks
    TasksRestController controller;

    @Test
    @DisplayName("GET /api/task возвращает HTTP-ответ со статусом 200 OK и списком задач")
    void getAllTasks_returnValidResponseEntity() {
        // given
        List<Task> tasks = List.of(new Task(UUID.randomUUID(), "Первая задача", false),
                new Task(UUID.randomUUID(), "Вторая задача", true));
        Mockito.doReturn(tasks).when(this.tasksRepository).findAll();

        // when
        ResponseEntity<List<Task>> responseEntity = this.controller.getAllTasks();

        // then
        Assertions.assertNotNull(responseEntity);
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assertions.assertEquals(MediaType.APPLICATION_JSON, responseEntity.getHeaders().getContentType());
        Assertions.assertEquals(tasks, responseEntity.getBody());
    }


    @Test
    void createNewTask_payloadIsValid_returnValidResponseEntity() {
        // given
        var details = "Третья задача";

        // when
        var responseEntity = this.controller.createNewTask(new NewTaskPayload(details),
                UriComponentsBuilder.fromUriString("http://localhost:8080"), Locale.ENGLISH);

        // then
        Assertions.assertNotNull(responseEntity);
        Assertions.assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        Assertions.assertEquals(MediaType.APPLICATION_JSON, responseEntity.getHeaders().getContentType());

        if (responseEntity.getBody() instanceof Task task) {
            Assertions.assertNotNull(task.getId());
            Assertions.assertEquals(details, task.getDetails());
            Assertions.assertFalse(task.isCompleted());
            Assertions.assertEquals(URI.create("http://localhost:8080/api/tasks/" + task.getId()),
                    responseEntity.getHeaders().getLocation());

            Mockito.verify(this.tasksRepository).save(task);
        } else {
            Assertions.assertInstanceOf(Task.class, responseEntity.getBody());
        }

        Mockito.verifyNoMoreInteractions(this.tasksRepository);
    }


    @Test
    void createNewTask_payloadIsValid_returnInvalidResponseEntity() {
        // given
        var details = "   ";
        var locale = Locale.US;
        var errorMessage = "Details is empty";

        Mockito.doReturn(errorMessage).when(this.messageSource).getMessage("tasks.create.details.errors.not_set", new Object[0], locale);

        // when
        var responseEntity = this.controller.createNewTask(new NewTaskPayload(details),
                UriComponentsBuilder.fromUriString("http://localhost:8080"), locale);

        // then
        Assertions.assertNotNull(responseEntity);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        Assertions.assertEquals(MediaType.APPLICATION_JSON, responseEntity.getHeaders().getContentType());
        Assertions.assertEquals(new ErrorsPresentation(List.of(errorMessage)), responseEntity.getBody());

        Mockito.verifyNoInteractions(this.tasksRepository);
    }
}