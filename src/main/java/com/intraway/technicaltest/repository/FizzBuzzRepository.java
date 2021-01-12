package com.intraway.technicaltest.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.intraway.technicaltest.abstract_.MDao;
import com.intraway.technicaltest.model.FizzBuzz;

/**
 * 
 * @author gustavo.rodriguez
 *
 */
@Repository
public interface FizzBuzzRepository extends MDao<FizzBuzz, Long> {
	
	
	@Query(value = "select f.* from fizzbuzz as f order by id desc", nativeQuery = true)
	List<FizzBuzz> findAll();
	
}
