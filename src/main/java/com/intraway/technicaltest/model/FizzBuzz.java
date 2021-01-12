package com.intraway.technicaltest.model;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * 
 * @author gustavo.rodriguez
 *
 */
@Getter
@Setter
@Entity
@Table(name = "fizzbuzz")
public class FizzBuzz {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private Long id;
	
	@NotNull
	@Column(name = "min")
	private int min;
	
	@NotNull
	@Column(name="max")
	private int max;
	
	@NotNull
	@Column(name="has_multiple_3")
	private boolean hasMultiple3;
	
	@Column(name="has_multiple_5")
	private boolean hasMultiple5;
	
	@NotNull
	@Column(name="created_date")
	private Date createdDate;
	
	@OneToMany(cascade = CascadeType.ALL,mappedBy = "fizzbuzz")
	private List<Item> items;
}
