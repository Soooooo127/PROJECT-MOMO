package com.momo.restaurant;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestRepository extends JpaRepository<Restaurant, Integer>{

	
	Optional< List<Restaurant>> findByCategory(String category); 
	List<Restaurant> findAll(Specification<Restaurant> spec);
}
