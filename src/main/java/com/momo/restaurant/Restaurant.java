package com.momo.restaurant;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
	
}
