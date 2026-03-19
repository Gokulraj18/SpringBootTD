package com.example.splunk.service;

import com.example.splunk.entity.Item;
import com.example.splunk.repository.ItemRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemServiceTest {

	@Mock
	private ItemRepository repository;

	@InjectMocks
	private ItemServiceImpl service;

	@Test
	void create_shouldSaveItem() {
		Item item = new Item(null, "Test");

		when(repository.save(any())).thenReturn(item);

		Item result = service.create(item);

		assertThat(result.getName()).isEqualTo("Test");
		verify(repository).save(item);
	}

	@Test
	void getById_shouldReturnItem() {
		Item item = new Item(1L, "Test");

		when(repository.findById(1L)).thenReturn(Optional.of(item));

		Item result = service.getById(1L);

		assertThat(result.getId()).isEqualTo(1L);
	}

	@Test
	void getById_shouldThrowException() {
		when(repository.findById(1L)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> service.getById(1L))
				.isInstanceOf(ItemNotFoundException.class);
	}

	@Test
	void delete_shouldCallRepository() {
		when(repository.existsById(1L)).thenReturn(true);

		service.delete(1L);

		verify(repository).deleteById(1L);
	}
}