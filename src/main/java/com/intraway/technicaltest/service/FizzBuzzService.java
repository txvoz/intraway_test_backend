package com.intraway.technicaltest.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.intraway.technicaltest.DTO.FizzBuzzDTO;
import com.intraway.technicaltest.DTO.ItemDTO;
import com.intraway.technicaltest.abstract_.BusinessServiceBase;
import com.intraway.technicaltest.abstract_.MDao;
import com.intraway.technicaltest.exception.ClientException;
import com.intraway.technicaltest.model.FizzBuzz;
import com.intraway.technicaltest.model.Item;
import com.intraway.technicaltest.repository.FizzBuzzRepository;


/**
 * 
 * @author gustavo.rodriguez
 *
 */
@Service
@Transactional(readOnly = true)
public class FizzBuzzService extends BusinessServiceBase<FizzBuzz, Long> {

	@Autowired
	private FizzBuzzRepository repository;

	
	@Override
	protected MDao<FizzBuzz, Long> getDao() {
		return repository;
	}


	public FizzBuzz getEntity(FizzBuzzDTO dto) {
		FizzBuzz entity = new FizzBuzz();
		entity.setMin(dto.getMin());
		entity.setMax(dto.getMax());
		return entity;
	}
	

	public FizzBuzzDTO getDto(FizzBuzz entity) {
		FizzBuzzDTO dto = new FizzBuzzDTO();
		dto.setId(entity.getId());
		dto.setHasMultiple3(entity.isHasMultiple3());
		dto.setHasMultiple5(entity.isHasMultiple5());
		dto.setMin(entity.getMin());
		dto.setMax(entity.getMax());
		dto.setCreatedDate(entity.getCreatedDate());
		dto.setItems(new ArrayList<ItemDTO>());
		for (Item item : entity.getItems()) {
			ItemDTO iDto = new ItemDTO();
			iDto.setValue(item.getValue());
			iDto.setConverterValue(item.getConverterValue());
			dto.getItems().add(iDto);
		}
		return dto;
	}
	
	public void validateEntity(FizzBuzz entity, boolean create) {
		if (create) {
			if(entity.getMin() > entity.getMax()) {
				throw new ClientException("Los parametros enviados son incorrectos", HttpStatus.BAD_REQUEST);
			}
			
			entity.setItems(new ArrayList<Item>());
			
			boolean hasMultiple3 = false;
			boolean hasMultiple5 = false;
			
			for(int i = entity.getMin(); i <= entity.getMax(); i++) {
				Item item = new Item();
				item.setCreatedDate(new Date());
				item.setValue(i);
				if(i % 3 == 0) {
					item.setMultiple3(true);
					item.setConverterValue("Fizz");
					hasMultiple3 = true;
				}else {
					item.setMultiple3(false);
					item.setConverterValue(item.getValue()+"");
				}
				
				if(i % 5 == 0) {
					item.setMultiple5(true);
					String cValue = "";
					if(i % 3 == 0) {
						cValue = item.getConverterValue();
						cValue += "Buzz";
					}else {
						cValue = "Buzz";
					}
					item.setConverterValue(cValue);
					hasMultiple5 = true;
				}else {
					item.setMultiple5(false);
				}
				
				item.setFizzbuzz(entity);
				entity.getItems().add(item);
			}
			
			entity.setHasMultiple3(hasMultiple3);
			entity.setHasMultiple5(hasMultiple5);
			entity.setCreatedDate(new Date());
		}
	}
	
	
	public List<FizzBuzz> findAll(){
		return repository.findAll();
	}
	
	@Override
	@Transactional
	public FizzBuzz create(FizzBuzz entity) {
		validateEntity(entity, true);
		super.save(entity);
		return entity;
	}

	
}