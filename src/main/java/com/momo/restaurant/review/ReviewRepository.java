package com.momo.restaurant.review;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.momo.restaurant.Restaurant;

public interface ReviewRepository extends JpaRepository<Review, Integer>{

	List<Review> findByRest(Restaurant rest);
	
	
	
	
}
