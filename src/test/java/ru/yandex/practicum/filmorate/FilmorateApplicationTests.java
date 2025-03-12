package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = FilmorateApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FilmorateApplicationTests {

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	void usersEndpoint() {
        String userJson = ("{\"id\":1, \"email\":\"user@example.com\", \"login\":\"dolore\", \"name\":\"User Name\"," +
				" \"birthday\":\"1946-08-20\"}");
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> request =new HttpEntity<>(userJson, headers);
		ResponseEntity<String> response= restTemplate.exchange("/users", HttpMethod.POST, request,
				String.class);
        assertEquals(200, response.getStatusCodeValue());
		String body = response.getBody();
        assertNotNull(body);
		assertTrue(body.contains("\"email\":\"user@example.com\""));
		assertTrue(body.contains("\"login\":\"dolore\""));
		assertTrue(body.contains("\"name\":\"User Name\""));
		assertTrue(body.contains("\"birthday\":\"1946-08-20\""));
		userJson = ("{\"id\":1, \"email\":\"user2@example.com\", \"login\":\"dolore2\", \"name\":\"User Name2\"," +
                " \"birthday\":\"1946-08-20\"}");
        request =new HttpEntity<>(userJson, headers);
        response = restTemplate.exchange("/users", HttpMethod.PUT,request, String.class);
        assertEquals(200, response.getStatusCodeValue());
        body = response.getBody();
        assertNotNull(body);
        assertTrue(body.contains("\"email\":\"user2@example.com\""));
        assertTrue(body.contains("\"login\":\"dolore2\""));
        assertTrue(body.contains("\"name\":\"User Name2\""));
        assertTrue(body.contains("\"birthday\":\"1946-08-20\""));
        assertTrue(body.contains("\"id\":1"));
		request = new HttpEntity<>("", headers);
		response = restTemplate.exchange("/users", HttpMethod.GET, request, String.class);
		assertEquals(200, response.getStatusCode().value());
		body = response.getBody();
		assertNotNull(body);
        assertTrue(body.contains("\"email\":\"user2@example.com\""));
        assertTrue(body.contains("\"login\":\"dolore2\""));
        assertTrue(body.contains("\"name\":\"User Name2\""));
        assertTrue(body.contains("\"birthday\":\"1946-08-20\""));
        assertTrue(body.contains("\"id\":1"));
	}

	@Test
	void filmsEndpoint() {
		String filmJson = ("{\"id\":1, \"name\":\"film\", \"description\":\"film description\", " +
				"\"releaseDate\":\"1946-08-20\", \"duration\":120}");
        HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> request =new HttpEntity<>(filmJson, headers);
		ResponseEntity<String> response= restTemplate.exchange("/films", HttpMethod.POST, request,
				String.class);
		assertEquals(200, response.getStatusCodeValue());
		String body = response.getBody();
		assertNotNull(body);
		assertTrue(body.contains("\"name\":\"film\""));
        assertTrue(body.contains("\"description\":\"film description\""));
        assertTrue(body.contains("\"releaseDate\":\"1946-08-20\""));
        assertTrue(body.contains("\"duration\":120"));
        filmJson = ("{\"id\":1, \"name\":\"film2\", \"description\":\"film description2\", \"releaseDate\":\"1946-08-20\"," +
                " \"duration\":120}");
        request =new HttpEntity<>(filmJson, headers);
        response = restTemplate.exchange("/films", HttpMethod.PUT,request, String.class);
        assertEquals(200, response.getStatusCodeValue());
        body = response.getBody();
        assertNotNull(body);
        assertTrue(body.contains("\"name\":\"film2\""));
        assertTrue(body.contains("\"description\":\"film description2\""));
        assertTrue(body.contains("\"releaseDate\":\"1946-08-20\""));
        assertTrue(body.contains("\"duration\":120"));
        assertTrue(body.contains("\"id\":1"));
        request = new HttpEntity<>("", headers);
        response = restTemplate.exchange("/films", HttpMethod.GET, request, String.class);
        assertEquals(200, response.getStatusCode().value());
        body = response.getBody();
	}

}
