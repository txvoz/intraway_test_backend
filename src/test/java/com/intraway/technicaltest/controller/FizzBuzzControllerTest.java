package com.intraway.technicaltest.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import com.intraway.technicaltest.service.FizzBuzzService;


@RunWith(SpringRunner.class)
@SpringBootTest
public class FizzBuzzControllerTest {

	 
	

	private static final String BASE_URL_API = "/intraway/api/fizzbuzz";
	
	@Autowired
	private WebApplicationContext wac;
	
	private MockMvc mockMvcClient;


	@Autowired
	private FizzBuzzService service;


	@BeforeEach
	public void setUp() throws Exception {
		mockMvcClient = MockMvcBuilders.webAppContextSetup(wac).build();
	}

	@Test
	public void findAll() throws Exception {
		mockMvcClient.perform(MockMvcRequestBuilders.get(BASE_URL_API).accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()));


	}

	@Test
	public void testFizzBuzzSuccess01() throws Exception {
		int min = 3;
		int max = 5;
		this.mockMvcClient.perform(MockMvcRequestBuilders.get(BASE_URL_API+"/"+min+"/"+max).accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()));
		
	}
	
	@Test
	public void testFizzBuzzErr01() throws Exception {
		int min = 5;
		int max = 3;
		this.mockMvcClient.perform(MockMvcRequestBuilders.get(BASE_URL_API+"/"+min+"/"+max).accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.BAD_REQUEST.value()));
		
	}
	

	
}
