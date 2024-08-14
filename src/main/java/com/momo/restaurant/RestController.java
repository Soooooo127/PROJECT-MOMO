package com.momo.restaurant;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/rest")
public class RestController {

	private final RestService restService;
	
	@GetMapping("/")
	public String restAll() {
		return "rest/rest_all";
	}
	
	@GetMapping("/detail")
	public String restDetail() {
		return "rest/rest_detail";
	}
	
	@GetMapping("/list")
	public String  restList() {
		
		return "rest/rest_list";
	}
	
	
}
