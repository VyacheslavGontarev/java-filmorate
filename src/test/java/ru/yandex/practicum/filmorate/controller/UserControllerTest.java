package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import ru.yandex.practicum.filmorate.FilmorateApplication;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = FilmorateApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

class UserControllerTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void userValidationPostTest() {
        String userJson = ("{\"id\":1, \"login\":\"dolore\", \"name\":\"User Name\"," +
                " \"birthday\":\"1946-08-20\"}");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(userJson, headers);
        ResponseEntity<String> response = restTemplate.exchange("/users", HttpMethod.POST, entity, String.class);
        assertEquals(500, response.getStatusCodeValue());
        userJson = ("{\"id\":1, \"email\":\"userexample.com\", \"login\":\"dolore\", \"name\":\"User Name\"," +
                        " \"birthday\":\"1946-08-20\"}");
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        entity = new HttpEntity<>(userJson, headers);
        response = restTemplate.exchange("/users", HttpMethod.POST, entity, String.class);
        assertEquals(500, response.getStatusCodeValue());
        userJson = ("{\"id\":1, \"email\":\"user@example.com\", \"name\":\"User Name\"," +
                        " \"birthday\":\"1946-08-20\"}");
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        entity = new HttpEntity<>(userJson, headers);
        response = restTemplate.exchange("/users", HttpMethod.POST, entity, String.class);
        assertEquals(500, response.getStatusCodeValue());
        userJson = ("{\"id\":1, \"email\":\"user@example.com\", \"login\":\"dol ore\", \"name\":\"User Name\"," +
                        " \"birthday\":\"1946-08-20\"}");
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        entity = new HttpEntity<>(userJson, headers);
        response = restTemplate.exchange("/users", HttpMethod.POST, entity, String.class);
        assertEquals(500, response.getStatusCodeValue());
        userJson = ("{\"id\":1, \"email\":\"user@example.com\", \"login\":\"dolore\", \"name\":\"User Name\"," +
                        " \"birthday\":\"2026-08-20\"}");
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        entity = new HttpEntity<>(userJson, headers);
        response = restTemplate.exchange("/users", HttpMethod.POST, entity, String.class);
        assertEquals(500, response.getStatusCodeValue());
        userJson = ("{}");
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        entity = new HttpEntity<>(userJson, headers);
        response = restTemplate.exchange("/users", HttpMethod.POST, entity, String.class);
        assertEquals(500, response.getStatusCodeValue());
    }

    @Test
    void userValidationPutTest() {
        String userJson = ("{\"id\":1, \"email\":\"user@example.com\", \"login\":\"dolore\", \"name\":\"User Name\"," +
                " \"birthday\":\"1946-08-20\"}");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(userJson, headers);
        ResponseEntity<String> response = restTemplate.exchange("/users", HttpMethod.POST, entity,
                String.class);
        assertEquals(200, response.getStatusCodeValue());
        userJson = ("{\"id\":1, \"email\":\"userexample.com\", \"login\":\"dolore\", \"name\":\"User Name\"," +
                " \"birthday\":\"1946-08-20\"}");
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        entity = new HttpEntity<>(userJson, headers);
        response = restTemplate.exchange("/users", HttpMethod.PUT, entity, String.class);
        assertEquals(500, response.getStatusCodeValue());
        userJson = ("{\"id\":1, \"email\":\"user@example.com\", \"login\":\"dol ore\", \"name\":\"User Name\"," +
                " \"birthday\":\"1946-08-20\"}");
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        entity = new HttpEntity<>(userJson, headers);
        response = restTemplate.exchange("/users", HttpMethod.PUT, entity, String.class);
        assertEquals(500, response.getStatusCodeValue());
        userJson = ("{\"id\":1, \"email\":\"user@example.com\", \"login\":\"dolore\", \"name\":\"User Name\"," +
                " \"birthday\":\"2026-08-20\"}");
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        entity = new HttpEntity<>(userJson, headers);
        response = restTemplate.exchange("/users", HttpMethod.PUT, entity, String.class);
        assertEquals(500, response.getStatusCodeValue());
        userJson = ("{}");
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        entity = new HttpEntity<>(userJson, headers);
        response = restTemplate.exchange("/users", HttpMethod.PUT, entity, String.class);
        assertEquals(500, response.getStatusCodeValue());
        userJson = ("{\"email\":\"user@example.com\", \"login\":\"dolore\", \"name\":\"User Name\"," +
                " \"birthday\":\"2026-08-20\"}");
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        entity = new HttpEntity<>(userJson, headers);
        response = restTemplate.exchange("/users", HttpMethod.PUT, entity, String.class);
        assertEquals(500, response.getStatusCodeValue());
    }
}