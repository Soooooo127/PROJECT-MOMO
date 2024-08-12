package com.momo.board.inquiry.comment;

import java.time.LocalDateTime;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import com.momo.board.inquiry.posting.InquiryPosting;
import com.momo.user.SiteUser.SiteUser;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@DynamicInsert
public class InquiryComment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(columnDefinition = "TEXT")
	private String content;
	
    private LocalDateTime createDate;
   
    private LocalDateTime updateDate;
    
    @ManyToOne
    private InquiryPosting inquiryPosting;
    
    @ManyToOne
    private SiteUser author;
    
}
