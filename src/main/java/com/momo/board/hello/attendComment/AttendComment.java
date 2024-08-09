package com.momo.board.hello.attendComment;

import java.time.LocalDateTime;

import com.momo.board.hello.attendCalendar.AttendCalendar;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class AttendComment 
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer attendCommentId;
	
	@Column(columnDefinition = "TEXT")
	private String content;

	private LocalDateTime createDate;
	
	private String memberNickname;
	
	private String memberUsername;
	
	@ManyToOne
	private AttendCalendar attendCalendar;
}
