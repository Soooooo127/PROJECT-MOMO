package com.momo.board.hello.attendCalendar;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("attendCalendar")
public class AttendCalendarController 
{
	private final AttendCalendarService attendCalendarService;
	
	@GetMapping("/list")
	public String getPostingList(Model model)
	{
		List<AttendCalendar> attendCalendarList = this.attendCalendarService.getList();
		model.addAttribute("attendCalendarList", attendCalendarList);
		return "attendCalendar_list";
	}
	
	
}
