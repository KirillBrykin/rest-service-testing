package ru.example.restservicetesting.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.example.restservicetesting.model.Task;
import ru.example.restservicetesting.repository.TasksRepositoryImpl;

import java.util.List;
import java.util.UUID;

//интеграционные тесты
@SpringBootTest
@AutoConfigureMockMvc(printOnlyOnFailure = false)
class TasksRestControllerIT {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    TasksRepositoryImpl tasksRepository;

    @AfterEach
    void terDown() {
        this.tasksRepository.getTasks().clear();
    }

    @Test
    void getAllTasks_returnValidResponseEntity() throws Exception {
        // given
        var requestBuilder = MockMvcRequestBuilders.get("/api/task");
        this.tasksRepository.getTasks().addAll(List.of(
                new Task(UUID.fromString("71117396-8694-11ed-9ef6-77042ee83937"), "Первая задача", false),
                new Task(UUID.fromString("7172d834-8694-11ed-8669-d7b17d45fba8"), "Вторая задача", true)
        ));

        // when
        this.mockMvc.perform(requestBuilder)
                // then
                .andExpectAll(
                        MockMvcResultMatchers.status().isOk(),
                        MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON),
                        MockMvcResultMatchers.content().json("""
                                [
                                    {
                                        "id": "71117396-8694-11ed-9ef6-77042ee83937",
                                        "details": "Первая задача",
                                        "completed": false
                                    },
                                    {
                                        "id": "7172d834-8694-11ed-8669-d7b17d45fba8",
                                        "details": "Вторая задача",
                                        "completed": true
                                    }
                                ]
                                """)
                );
    }

    @Test
    void createNewTask_payloadIsValid_returnValidResponseEntity() throws Exception {
        // given
        var requestBuilder = MockMvcRequestBuilders.post("/api/task")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "details": "Третья задача"
                        }
                        """);

        // when
        this.mockMvc.perform(requestBuilder)
                // then
                .andExpectAll(
                        MockMvcResultMatchers.status().isCreated(),
                        MockMvcResultMatchers.header().exists(HttpHeaders.LOCATION),
                        MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON),
                        MockMvcResultMatchers.content().json("""
                                    {
                                        "details": "Третья задача",
                                        "completed": false
                                    }
                                """),
                        MockMvcResultMatchers.jsonPath("$.id").exists()
                );

        Assertions.assertEquals(1, this.tasksRepository.getTasks().size());
        final var task = this.tasksRepository.getTasks().get(0);
        Assertions.assertNotNull(task.getId());
        Assertions.assertEquals("Третья задача", task.getDetails());
        Assertions.assertFalse(task.isCompleted());
    }

    @Test
    void createNewTask_payloadIsValid_returnInvalidResponseEntity() throws Exception {
        // given
        var requestBuilder = MockMvcRequestBuilders.post("/api/task")
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.ACCEPT_LANGUAGE, "en")
                .content("""
                        {
                            "details": null
                        }
                        """);

        // when
        this.mockMvc.perform(requestBuilder)
                // then
                .andExpectAll(
                        MockMvcResultMatchers.status().isBadRequest(),
                        MockMvcResultMatchers.header().doesNotExist(HttpHeaders.LOCATION),
                        MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON),
                        MockMvcResultMatchers.content().json("""
                                    {
                                        "errors": ["Task details must be set"]
                                    }
                                """, true)
                );

        Assertions.assertTrue(this.tasksRepository.getTasks().isEmpty());
    }

}