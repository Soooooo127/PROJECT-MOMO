package com.momo.restaurant;

import java.util.List;

import com.momo.restaurant.et.EatTogether;
import com.momo.restaurant.review.Review;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Restaurant {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer no;
	
	@NotNull
	private String name;
	
	private String category;
	
	private String addr;
		
	private String phone;
	
	private String menu;
	
	private String img;
	
	private String map1;
	
	private String map2;

	@OneToMany(mappedBy = "rest", cascade = CascadeType.REMOVE )
	private List<Review> reviewList;
	
	@OneToMany(mappedBy = "rest" , cascade = CascadeType.REMOVE)
	private List<EatTogether> etList;
	
	private Integer progresset;
}
