package com.momo.board.hello.attendCalendar;

import java.util.List;

import com.momo.board.hello.attendComment.AttendComment;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class AttendCalendar 
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer attendCalendarId;
	
	private int year;
	
	private int month;
	
	private int day;
	
	@OneToMany(mappedBy = "attendCalendar")
	private List<AttendComment> attendCommentList;
}
