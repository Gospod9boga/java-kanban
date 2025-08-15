package http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BaseHttpHandler {
    protected static final String GET = "GET";
    protected static final String POST = "POST";
    protected static final String DELETE = "DELETE";

    protected static final int OK = 200;
    protected static final int CREATED = 201;
    protected static final int BAD_REQUEST = 400;
    protected static final int NOT_FOUND = 404;
    protected static final int NOT_ACCEPTABLE = 406;

    public static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .create();

    protected void sendResponse(HttpExchange exchange, int statusCode, String contentType, String body) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", contentType);
        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(statusCode, bytes.length);
        exchange.getResponseBody().write(bytes);
        exchange.close();
    }

    protected void sendTextOk(HttpExchange exchange, Object responseObject) throws IOException {
        if (responseObject instanceof String) {
            sendResponse(exchange, OK, "text/plain; charset=UTF-8", (String) responseObject);
        } else {
            sendResponse(exchange, OK, "application/json; charset=UTF-8", GSON.toJson(responseObject));
        }
    }

    protected void sendIdNotFound(HttpExchange exchange, int id) throws IOException {
        sendResponse(exchange, NOT_FOUND, "text/plain; charset=UTF-8", "Задача с id " + id + " не найдена");
    }

    protected void sendIncorrectId(HttpExchange exchange, String id) throws IOException {
        sendResponse(exchange, BAD_REQUEST, "text/plain; charset=UTF-8", "Некорректный id: " + id);
    }

    protected void sendTextCreatedOk(HttpExchange exchange, String response) throws IOException {
        sendResponse(exchange, CREATED, "text/plain; charset=UTF-8", response);
    }

    protected void sendHasOverlaps(HttpExchange exchange) throws IOException {
        sendResponse(exchange, NOT_ACCEPTABLE, "text/plain; charset=UTF-8", "Задача пересекается с существующими");
    }

    public static class LocalDateTimeAdapter extends TypeAdapter<LocalDateTime> {
        private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

        @Override
        public void write(JsonWriter out, LocalDateTime value) throws IOException {
            out.value(value != null ? value.format(formatter) : null);
        }

        @Override
        public LocalDateTime read(JsonReader in) throws IOException {
            return LocalDateTime.parse(in.nextString(), formatter);
        }
    }

    public static class DurationAdapter extends TypeAdapter<Duration> {
        @Override
        public void write(JsonWriter out, Duration value) throws IOException {
            out.value(value != null ? value.toString() : null);
        }

        @Override
        public Duration read(JsonReader in) throws IOException {
            return Duration.parse(in.nextString());
        }
    }
}