package com.example.splunk.controller;

import com.example.splunk.entity.Item;
import com.example.splunk.repository.ItemRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ItemControllerIT {

	@Value("${local.server.port}")
	int port;

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	ItemRepository itemRepository;

	@Test
	void crudFlow_shouldWork() throws Exception {
		itemRepository.deleteAll();

		HttpClient client = HttpClient.newHttpClient();

		// Create
		HttpRequest createRequest = HttpRequest.newBuilder()
				.uri(URI.create("http://localhost:" + port + "/api/items"))
				.header("Content-Type", "application/json")
				.POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(new Item(null, "First"))))
				.build();
		HttpResponse<String> createResponse = client.send(createRequest, HttpResponse.BodyHandlers.ofString());
		assertThat(createResponse.statusCode()).isEqualTo(201);
		Item created = objectMapper.readValue(createResponse.body(), Item.class);
		assertThat(created.getId()).isNotNull();
		assertThat(created.getName()).isEqualTo("First");

		// Read by id
		HttpRequest getRequest = HttpRequest.newBuilder()
				.uri(URI.create("http://localhost:" + port + "/api/items/" + created.getId()))
				.GET()
				.build();
		HttpResponse<String> getResponse = client.send(getRequest, HttpResponse.BodyHandlers.ofString());
		assertThat(getResponse.statusCode()).isEqualTo(200);
		Item fetched = objectMapper.readValue(getResponse.body(), Item.class);
		assertThat(fetched.getId()).isEqualTo(created.getId());
		assertThat(fetched.getName()).isEqualTo("First");

		// List
		HttpRequest listRequest = HttpRequest.newBuilder()
				.uri(URI.create("http://localhost:" + port + "/api/items"))
				.GET()
				.build();
		HttpResponse<String> listResponse = client.send(listRequest, HttpResponse.BodyHandlers.ofString());
		assertThat(listResponse.statusCode()).isEqualTo(200);
		Item[] items = objectMapper.readValue(listResponse.body(), Item[].class);
		assertThat(Arrays.asList(items)).hasSize(1);

		// Update
		HttpRequest updateRequest = HttpRequest.newBuilder()
				.uri(URI.create("http://localhost:" + port + "/api/items/" + created.getId()))
				.header("Content-Type", "application/json")
				.PUT(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(new Item(null, "Updated"))))
				.build();
		HttpResponse<String> updateResponse = client.send(updateRequest, HttpResponse.BodyHandlers.ofString());
		assertThat(updateResponse.statusCode()).isEqualTo(200);
		Item updated = objectMapper.readValue(updateResponse.body(), Item.class);
		assertThat(updated.getName()).isEqualTo("Updated");

		// Delete
		HttpRequest deleteRequest = HttpRequest.newBuilder()
				.uri(URI.create("http://localhost:" + port + "/api/items/" + created.getId()))
				.DELETE()
				.build();
		HttpResponse<String> deleteResponse = client.send(deleteRequest, HttpResponse.BodyHandlers.ofString());
		assertThat(deleteResponse.statusCode()).isEqualTo(204);

		// Read missing
		HttpResponse<String> missing = client.send(getRequest, HttpResponse.BodyHandlers.ofString());
		assertThat(missing.statusCode()).isEqualTo(404);
	}
}

