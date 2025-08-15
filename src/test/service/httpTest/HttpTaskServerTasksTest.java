package test.service.httpTest;

import com.google.gson.Gson;
import http.server.HttpTaskServer;
import manager.Managers;
import manager.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import task.TaskStatus;
import task.Task;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HttpTaskServerTasksTest {
    private HttpTaskServer server;
    private TaskManager manager;
    private HttpClient client;
    private Gson gson;

    @BeforeEach
    public void setUp() throws IOException {
        manager = Managers.getDefault();
        server = new HttpTaskServer(manager);
        gson = server.getGson();
        server.start();
        client = HttpClient.newHttpClient();
    }

    @AfterEach
    public void tearDown() {
        server.stop();
    }

    @Test
    public void testGetEmptyTasksList() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertEquals("[]", response.body().trim());
    }

    @Test
    public void testCreateTask() throws IOException, InterruptedException {
        Task task = new Task(0, "Test", TaskStatus.NEW, "Testing task", Duration.ofMinutes(5), LocalDateTime.now());

        String json = gson.toJson(task);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());
        assertEquals("Задача успешно создана", response.body());
    }

    @Test
    public void testGetTaskById() throws IOException, InterruptedException {
        Task task = new Task(0, "Test", TaskStatus.NEW, "Testing task", Duration.ofMinutes(5), LocalDateTime.now());

        String json = gson.toJson(task);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        client.send(request, HttpResponse.BodyHandlers.ofString());

        HttpRequest request1 = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/1"))
                .GET()
                .build();

        HttpResponse<String> response1 = client.send(request1, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response1.statusCode());
        assertTrue(response1.body().contains("\"id\":1"));
    }

    @Test
    public void testGetAllTasks() throws IOException, InterruptedException {
        Task task1 = new Task(0, "Test", TaskStatus.NEW, "Testing task", Duration.ofMinutes(5), LocalDateTime.of(2025, Month.JUNE, 13, 12, 13));
        Task task2 = new Task(0, "Test2", TaskStatus.NEW, "Testing task 2", Duration.ofMinutes(2), LocalDateTime.of(2024, Month.JUNE, 12, 22, 12));

        String jsonTask1 = gson.toJson(task1);
        HttpRequest request1 = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonTask1))
                .build();
        client.send(request1, HttpResponse.BodyHandlers.ofString());

        String jsonTask2 = gson.toJson(task2);
        HttpRequest request2 = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonTask2))
                .build();
        client.send(request2, HttpResponse.BodyHandlers.ofString());

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String tasks = gson.toJson(manager.getAllTask());
        assertEquals(200, response.statusCode());
        assertEquals(response.body(), tasks);
    }

    @Test
    public void testTaskNotFound() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/1"))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(404, response.statusCode());
    }

    @Test
    public void testDeleteTask() throws IOException, InterruptedException {
        Task task = new Task(0, "Test", TaskStatus.NEW, "Testing task", Duration.ofMinutes(5), LocalDateTime.now());
        String json = gson.toJson(task);

        HttpRequest createRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        client.send(createRequest, HttpResponse.BodyHandlers.ofString());

        HttpRequest deleteRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/1"))
                .DELETE()
                .build();

        HttpResponse<String> deleteResponse = client.send(deleteRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, deleteResponse.statusCode());

        HttpRequest getRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/1"))
                .GET()
                .build();

        HttpResponse<String> getResponse = client.send(getRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, getResponse.statusCode());
    }

    @Test
    public void testCreateTaskWithTimeOverlap() throws IOException, InterruptedException {
        LocalDateTime startTime1 = LocalDateTime.of(2025, 1, 1, 10, 0);
        Task task1 = new Task(0, "Task1", TaskStatus.NEW, "Desc1", Duration.ofHours(2), startTime1);
        String jsonTask1 = gson.toJson(task1);

        HttpRequest createRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonTask1))
                .build();
        client.send(createRequest, HttpResponse.BodyHandlers.ofString());

        LocalDateTime startTime2 = LocalDateTime.of(2025, 1, 1, 11, 0);
        Task task2 = new Task(0, "Task2", TaskStatus.NEW, "Desc2", Duration.ofHours(2), startTime2);
        String jsonTask2 = gson.toJson(task2);
        HttpRequest createRequest2 = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonTask2))
                .build();
        HttpResponse<String> response = client.send(createRequest2, HttpResponse.BodyHandlers.ofString());
        assertEquals(406, response.statusCode());
    }

    @Test
    public void testGetTaskWithInvalidId() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/a"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(400, response.statusCode());
        assertTrue(response.body().contains("Некорректный id"));
    }
}

