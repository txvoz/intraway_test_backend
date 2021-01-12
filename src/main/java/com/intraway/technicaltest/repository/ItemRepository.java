package com.intraway.technicaltest.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.intraway.technicaltest.abstract_.MDao;
import com.intraway.technicaltest.model.Item;

/**
 * 
 * @author gustavo.rodriguez
 *
 */
@Repository
public interface ItemRepository extends MDao<Item, Long> {
	
	@Query(value = "select i.* from items as i where i.fizzbuzz = ?1 ", nativeQuery = true)
	public List<Item> findAllByByFizzBuzzId(Long fizzbuzzId);
}
