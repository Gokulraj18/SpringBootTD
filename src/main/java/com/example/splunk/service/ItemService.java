package com.example.splunk.service;

import com.example.splunk.entity.Item;

import java.util.List;

public interface ItemService {
	Item create(Item item);

	Item getById(long id);

	List<Item> getAll();

	Item update(long id, Item item);

	void delete(long id);
}
