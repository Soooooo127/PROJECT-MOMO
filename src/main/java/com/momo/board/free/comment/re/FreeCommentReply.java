package com.momo.board.free.comment.re;

import java.time.LocalDateTime;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import com.momo.board.free.comment.FreeComment;
import com.momo.member.Member;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
@DynamicInsert
public class FreeCommentReply {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer no;
	
	@Column(columnDefinition = "TEXT")
	private String content;
	
	private String membernick;
	
	private LocalDateTime createDate;
	
	private LocalDateTime updateDate;

	@ColumnDefault("0")
	private Integer ddabong;
	
	@ColumnDefault("0")
	private Integer nope;
	
	@ManyToOne
	private Member author;
	
	@ManyToOne
	private FreeComment freeComment;
	
}
