package com.intraway.technicaltest.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.intraway.technicaltest.DTO.ItemDTO;
import com.intraway.technicaltest.abstract_.BusinessServiceBase;
import com.intraway.technicaltest.abstract_.MDao;
import com.intraway.technicaltest.model.Item;
import com.intraway.technicaltest.repository.ItemRepository;

/**
 * 
 * @author gustavo.rodriguez
 *
 */
@Service
@Transactional(readOnly = true)
public class ItemService extends BusinessServiceBase<Item, Long> {

	@Autowired
	private ItemRepository repository;

	
	@Override
	protected MDao<Item, Long> getDao() {
		return repository;
	}


	public Item getEntity(ItemDTO dto) {
		Item entity = new Item();
		return entity;
	}
	

	public ItemDTO getDto(Item entity) {
		ItemDTO dto = new ItemDTO();
		dto.setConverterValue(entity.getConverterValue());
		dto.setValue(entity.getValue());
		return dto;
	}
	
	public List<ItemDTO> getDtos(List<Item> entities){
		List<ItemDTO> items = new ArrayList<ItemDTO>();
		for (Item item : entities) {
			ItemDTO dto = getDto(item);
			items.add(dto);
		}
		return items;
	}
	
	public void validateEntity(Item entity, boolean create) {
		if(create) {
			
		}
	}
	
	
	@Override
	@Transactional
	public Item create(Item entity) {
		validateEntity(entity, true);
		super.save(entity);
		return entity;
	}

	public List<Item> findAllByByFizzBuzzId(Long fizzbuzzId){
		return repository.findAllByByFizzBuzzId(fizzbuzzId);
	}
	
}