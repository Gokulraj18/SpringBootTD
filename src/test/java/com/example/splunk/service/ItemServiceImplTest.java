package com.example.splunk.service;

import com.example.splunk.entity.Item;
import com.example.splunk.repository.ItemRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemServiceImplTest {

	@Mock
	ItemRepository itemRepository;

	@InjectMocks
	ItemServiceImpl itemService;

	@Test
	void create_shouldNullOutIdAndSave() {

		Item input = new Item(999L, "A");

		when(itemRepository.save(any(Item.class)))
				.thenAnswer(inv -> inv.getArgument(0));

		Item saved = itemService.create(input);

		assertThat(saved.getId()).isNull();
		assertThat(saved.getName()).isEqualTo("A");
	}

	@Test
	void getById_shouldThrow404WhenMissing() {

		when(itemRepository.findById(10L))
				.thenReturn(Optional.empty());

		assertThatThrownBy(() -> itemService.getById(10L))
				.isInstanceOf(ItemNotFoundException.class);
	}

	@Test
	void update_shouldUpdateName() {

		Item existing = new Item(1L, "Old");

		when(itemRepository.findById(1L))
				.thenReturn(Optional.of(existing));

		when(itemRepository.save(any(Item.class)))
				.thenAnswer(inv -> inv.getArgument(0));

		Item updated = itemService.update(1L, new Item(null, "New"));

		assertThat(updated.getId()).isEqualTo(1L);
		assertThat(updated.getName()).isEqualTo("New");

		ArgumentCaptor<Item> captor = ArgumentCaptor.forClass(Item.class);

		verify(itemRepository).save(captor.capture());

		assertThat(captor.getValue().getName()).isEqualTo("New");
	}

	@Test
	void delete_shouldThrowWhenMissing() {

		when(itemRepository.existsById(5L))
				.thenReturn(false);

		assertThatThrownBy(() -> itemService.delete(5L))
				.isInstanceOf(ItemNotFoundException.class);

		verify(itemRepository, never()).deleteById(anyLong());
	}
}