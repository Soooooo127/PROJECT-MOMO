package com.momo.board.hello.attendCalendar;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AttendCalendarService 
{
	private final AttendCalendarRepository attendCalendarRepository;
	
	public List<AttendCalendar> getList()
	{
		List<AttendCalendar> attendCalendarList = this.attendCalendarRepository.findAll();
		return attendCalendarList;
	}
	
	Date today = new Date();
	
	@SuppressWarnings("deprecation")
	public void create()
	{
		AttendCalendar attendCalendar = new AttendCalendar();
		attendCalendar.setYear(today.getYear());
		attendCalendar.setMonth(today.getMonth());
		attendCalendar.setDay(today.getDay());
		this.attendCalendarRepository.save(attendCalendar);
	}
}
