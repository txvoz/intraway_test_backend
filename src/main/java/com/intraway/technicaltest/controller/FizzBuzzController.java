package com.intraway.technicaltest.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.intraway.technicaltest.DTO.FizzBuzzDTO;
import com.intraway.technicaltest.DTO.ItemDTO;
import com.intraway.technicaltest.DTO.SuccessResponse;
import com.intraway.technicaltest.enums.EStatusRequest;
import com.intraway.technicaltest.model.FizzBuzz;
import com.intraway.technicaltest.service.FizzBuzzService;
import com.intraway.technicaltest.service.ItemService;

/**
 * 
 * @author Gustavo Rodriguez
 *
 */
@CrossOrigin
@RestController
@RequestMapping("/intraway/api/fizzbuzz")
public class FizzBuzzController  {
	
	@Autowired
	private FizzBuzzService service;
	
	@Autowired
	private ItemService itemService;
		
	/**
	 * Metodo para devolver todos los request
	 * @return
	 */
	@GetMapping(path="", produces = "application/json")
	public ResponseEntity<SuccessResponse> getAll(){
		SuccessResponse r = new SuccessResponse();
		Date now = new Date();
		r.setTimestamp(now.getTime());
		List<FizzBuzz> entities = service.findAll();
		List<FizzBuzzDTO> dtos = new ArrayList<FizzBuzzDTO>();
		for (FizzBuzz fizzBuzz : entities) {
			dtos.add(service.getDto(fizzBuzz));
		}
		r.setData(dtos);
		r.setCode(EStatusRequest.E005.getCode());
		r.setDescription("Lista de request realizados");
		return new ResponseEntity<>(r, HttpStatus.OK);
	}
	
	/**
	 * Metodo para crear un nuevo request
	 * @param min
	 * @param max
	 * @return
	 */
	@GetMapping(path="/{min}/{max}", produces = "application/json")
	public ResponseEntity<SuccessResponse> testFizzBuzz(@PathVariable @Validated int min, @PathVariable @Validated int max) {
		SuccessResponse r = new SuccessResponse();
		FizzBuzzDTO dto = new FizzBuzzDTO();
		dto.setMin(min);
		dto.setMax(max);
		FizzBuzz entity = service.getEntity(dto);
		entity = service.create(entity);
		dto = service.getDto(entity);
		if(dto.isHasMultiple3() && dto.isHasMultiple5()) {
			r.setCode(EStatusRequest.E001.getCode());
			r.setDescription("Se encontraron multiplos de 3 y de 5");
		}else if(dto.isHasMultiple3() || dto.isHasMultiple5()) {
			r.setCode(EStatusRequest.E002.getCode());
			int mult;
			if(dto.isHasMultiple3()) {
				mult = 3;
			}else {
				mult = 5;
			}
			r.setDescription("Se encontraron multiplos de "+mult);
		}
		Date now = new Date();
		r.setTimestamp(now.getTime());
		List<ItemDTO> itemsDtos = itemService.getDtos(itemService.findAllByByFizzBuzzId(dto.getId()));
		r.setData(itemsDtos);
		return new ResponseEntity<>(r, HttpStatus.OK);
	}
	
		
}

