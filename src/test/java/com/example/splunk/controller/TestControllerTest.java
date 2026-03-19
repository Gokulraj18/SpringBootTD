package com.example.splunk.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class TestControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void testEndpoint_shouldReturnServerIsRunning() throws Exception {
		mockMvc.perform(get("/test"))
				.andExpect(status().isOk())
				.andExpect(content().string("Server is running"));
	}
}