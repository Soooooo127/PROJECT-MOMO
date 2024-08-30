package com.momo.restaurant.time;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;

import com.momo.restaurant.RestService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class TimeController {

	private final RestService restService;
	private final TimeService timeService;
	/*
	public String time(Model model, @PathVariable("no") Integer no) {
		
		
		
	}
	*/
	
	
	
	
}
