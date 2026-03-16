package com.example.splunk.service;

import com.example.splunk.entity.Item;
import com.example.splunk.repository.ItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ItemServiceImpl implements ItemService {

	private final ItemRepository itemRepository;

	public ItemServiceImpl(ItemRepository itemRepository) {
		this.itemRepository = itemRepository;
	}

	@Override
	public Item create(Item item) {
		item.setId(null);
		return itemRepository.save(item);
	}

	@Override
	@Transactional(readOnly = true)
	public Item getById(long id) {
		return itemRepository.findById(id)
				.orElseThrow(() -> new ItemNotFoundException(id));
	}

	@Override
	@Transactional(readOnly = true)
	public List<Item> getAll() {
		return itemRepository.findAll();
	}

	@Override
	public Item update(long id, Item item) {
		Item existing = getById(id);
		existing.setName(item.getName());
		return itemRepository.save(existing);
	}

	@Override
	public void delete(long id) {
		if (!itemRepository.existsById(id)) {
			throw new ItemNotFoundException(id);
		}
		itemRepository.deleteById(id);
	}
}

