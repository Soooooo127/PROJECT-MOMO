package com.momo.board.faq.posting;

import java.time.LocalDateTime;

import org.hibernate.annotations.ColumnDefault;

import com.momo.board.faq.category.FaqCategory;
import com.momo.member.Member;

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
public class FaqPosting {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer no;
	
	@Column(length=200)
	private String subject;
	
	@Column(columnDefinition ="TEXT")
	private String content;
	
	private LocalDateTime createDate;
	
	private LocalDateTime updateDate;
	
	@ColumnDefault("0")
	private Integer cnt;
	
	
	@ManyToOne
	private FaqCategory faqCategory;
	
	@ManyToOne
	private Member author;
}
