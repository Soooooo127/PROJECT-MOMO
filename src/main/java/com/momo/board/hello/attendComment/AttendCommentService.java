package com.momo.board.hello.attendComment;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.momo.board.hello.attendCalendar.AttendCalendar;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AttendCommentService 
{
	private final AttendCommentRepository attendCommentRepository;
	
	public void create(String content, AttendCalendar attendCalendar)
	{
		AttendComment attendComment = new AttendComment();
		
		attendComment.setContent(content);
		attendComment.setCreateDate(LocalDateTime.now());
		attendComment.setMemberNickname("ThisIsId");
		attendComment.setMemberUsername("ThisIsUsername");
		attendComment.setAttendCalendar(attendCalendar);
		this.attendCommentRepository.save(attendComment);
	}
}
