package com.intraway.technicaltest.model;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "items")
public class Item {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private Long id;
	
	@NotNull
	@Column(name = "value")
	private int value;
	
	@NotNull
	@Column(name="is_multiple_3")
	private boolean multiple3;
	
	@NotNull
	@Column(name="is_multiple_5")
	private boolean multiple5;
	
	@NotNull
	@Column(name = "converter_value")
	private String converterValue;
	
	@NotNull
	@Column(name="created_date")
	private Date createdDate;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="fizzbuzz")
	private FizzBuzz fizzbuzz;	
}
