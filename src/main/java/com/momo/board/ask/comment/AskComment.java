package com.momo.board.ask.comment;

import java.time.LocalDateTime;
import java.util.Set;

import org.hibernate.annotations.DynamicInsert;

import com.momo.board.ask.posting.AskPosting;
import com.momo.member.MomoMember;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@DynamicInsert
public class AskComment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer no;
	
	@Column(columnDefinition = "TEXT")
	private String content;
	
	private LocalDateTime createDate;
	
	private LocalDateTime updateDate;
	
	/*@Column(columnDefinition = "integer default 0")
	private Integer ddabong;*/
	
	@ManyToMany
	private Set<MomoMember> nope;
	
	@Column(columnDefinition = "integer default 0")
	private Integer cnt;
	
	@ManyToOne
	private MomoMember membernick;
	
	@ManyToOne
	private AskPosting askPosting;
	
	@ManyToMany
	private Set<MomoMember> ddabong;
}
