package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import ru.yandex.practicum.filmorate.FilmorateApplication;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = FilmorateApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

class FilmControllerTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void filmValidationPostTest() {
        String filmJson = ("{\"id\":1, \"name\":\"\", \"description\":\"film description\", " +
                "\"releaseDate\":\"1946-08-20\"," + " \"duration\":120}");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request =new HttpEntity<>(filmJson, headers);
        ResponseEntity<String> response= restTemplate.exchange("/films", HttpMethod.POST, request,
                String.class);
        assertEquals(500, response.getStatusCodeValue());
        filmJson = ("{\"id\":1, \"description\":\"film description\", \"releaseDate\":\"1946-08-20\"," +
                " \"duration\":120}");
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        request =new HttpEntity<>(filmJson, headers);
        response= restTemplate.exchange("/films", HttpMethod.POST, request, String.class);
        assertEquals(500, response.getStatusCodeValue());
        filmJson = ("{\"id\":1, \"name\":\"film\", \"description\":\"film descriptionnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnn" +
                "nnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnn" +
                "nnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnn\", \"releaseDate\":\"1946-08-20\"," +
                " \"duration\":120}");
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        request =new HttpEntity<>(filmJson, headers);
        response= restTemplate.exchange("/films", HttpMethod.POST, request, String.class);
        assertEquals(500, response.getStatusCodeValue());
        filmJson = ("{\"id\":1, \"name\":\"film\", \"description\":\"film description\", " +
                "\"releaseDate\":\"1846-08-20\", \"duration\":120}");
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        request =new HttpEntity<>(filmJson, headers);
        response= restTemplate.exchange("/films", HttpMethod.POST, request, String.class);
        assertEquals(500, response.getStatusCodeValue());
        filmJson = ("{\"id\":1, \"name\":\"film\", \"description\":\"film description\", " +
                "\"releaseDate\":\"1946-08-20\", \"duration\":0}");
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        request =new HttpEntity<>(filmJson, headers);
        response= restTemplate.exchange("/films", HttpMethod.POST, request, String.class);
        assertEquals(500, response.getStatusCodeValue());
        filmJson = ("{}");
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        request =new HttpEntity<>(filmJson, headers);
        response= restTemplate.exchange("/films", HttpMethod.POST, request, String.class);
        assertEquals(500, response.getStatusCodeValue());
    }

    @Test
    void filmValidationPutTest() {
        String filmJson = ("{\"id\":1, \"name\":\"film\", \"description\":\"film description\", " +
                "\"releaseDate\":\"1946-08-20\", \"duration\":120}");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(filmJson, headers);
        ResponseEntity<String> response = restTemplate.exchange("/films", HttpMethod.POST, request,
                String.class);
        assertEquals(200, response.getStatusCodeValue());
        filmJson = ("{\"id\":1, \"name\":\"\", \"description\":\"film description\", " +
                "\"releaseDate\":\"1946-08-20\"," + " \"duration\":120}");
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        request = new HttpEntity<>(filmJson, headers);
        response = restTemplate.exchange("/films", HttpMethod.PUT, request,
                String.class);
        assertEquals(500, response.getStatusCodeValue());
        filmJson = ("{\"id\":1, \"name\":\"film\", \"description\":\"film descriptionnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnn" +
                "nnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnn" +
                "nnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnn\", \"releaseDate\":\"1946-08-20\"," +
                " \"duration\":120}");
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        request = new HttpEntity<>(filmJson, headers);
        response = restTemplate.exchange("/films", HttpMethod.PUT, request, String.class);
        assertEquals(500, response.getStatusCodeValue());
        filmJson = ("{\"id\":1, \"name\":\"film\", \"description\":\"film description\", " +
                "\"releaseDate\":\"1846-08-20\", \"duration\":120}");
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        request = new HttpEntity<>(filmJson, headers);
        response = restTemplate.exchange("/films", HttpMethod.PUT, request, String.class);
        assertEquals(500, response.getStatusCodeValue());
        filmJson = ("{\"id\":1, \"name\":\"film\", \"description\":\"film description\", " +
                "\"releaseDate\":\"1946-08-20\", \"duration\":0}");
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        request = new HttpEntity<>(filmJson, headers);
        response= restTemplate.exchange("/films", HttpMethod.PUT, request, String.class);
        assertEquals(500, response.getStatusCodeValue());
        filmJson = ("{}");
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        request = new HttpEntity<>(filmJson, headers);
        response = restTemplate.exchange("/films", HttpMethod.PUT, request, String.class);
        assertEquals(500, response.getStatusCodeValue());
        filmJson = ("{\"name\":\"film\", \"description\":\"film description\", " +
                "\"releaseDate\":\"1946-08-20\", \"duration\":0}");
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        request = new HttpEntity<>(filmJson, headers);
        response = restTemplate.exchange("/films", HttpMethod.PUT, request, String.class);
        assertEquals(500, response.getStatusCodeValue());
    }

}