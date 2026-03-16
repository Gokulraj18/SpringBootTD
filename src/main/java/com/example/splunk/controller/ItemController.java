package com.example.splunk.controller;

import com.example.splunk.entity.Item;
import com.example.splunk.service.ItemService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/items")
public class ItemController {

	private final ItemService itemService;

	public ItemController(ItemService itemService) {
		this.itemService = itemService;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Item create(@RequestBody Item item) {
		return itemService.create(item);
	}

	@GetMapping("/{id}")
	public Item getById(@PathVariable long id) {
		return itemService.getById(id);
	}

	@GetMapping
	public List<Item> getAll() {
		return itemService.getAll();
	}

	@PutMapping("/{id}")
	public Item update(@PathVariable long id, @RequestBody Item item) {
		return itemService.update(id, item);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable long id) {
		itemService.delete(id);
	}
}
