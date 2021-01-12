package com.intraway.technicaltest.DTO;

import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 
 * @author Gustavo Rodriguez
 *
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
public class FizzBuzzDTO {
	private Long id;
	private int min;
	private int max;
	private boolean hasMultiple3;
	private boolean hasMultiple5;
	private Date createdDate;
	private List<ItemDTO> items;
}