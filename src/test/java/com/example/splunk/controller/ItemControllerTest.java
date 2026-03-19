package com.example.splunk.controller;

import com.example.splunk.entity.Item;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ItemControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void crudFlow_shouldWork() throws Exception {

		// CREATE
		String response = mockMvc.perform(post("/api/items")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(new Item(null, "Test"))))
				.andExpect(status().isCreated())
				.andReturn().getResponse().getContentAsString();

		Item created = objectMapper.readValue(response, Item.class);

		// READ
		mockMvc.perform(get("/api/items/" + created.getId()))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name").value("Test"));

		// UPDATE
		mockMvc.perform(put("/api/items/" + created.getId())
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(new Item(null, "Updated"))))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name").value("Updated"));

		// DELETE
		mockMvc.perform(delete("/api/items/" + created.getId()))
				.andExpect(status().isNoContent());
	}
}