package com.momo.board.free.posting;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import com.momo.board.free.comment.FreeComment;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
@DynamicInsert
public class FreePosting {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer no;
	
	@Column(length = 200)
	private String subject;
	
	@Column(columnDefinition = "TEXT")
	private String content;
	
	private String memberid;
	
	private String membernick;
	
	private LocalDateTime createDate;
	
	private LocalDateTime updateDate;
	
//	@Column(columnDefinition = "Integer default 0")
	@ColumnDefault("0")
	private Integer cnt;
	
	@ColumnDefault("0")
	private Integer ddabong;
	
	@ColumnDefault("0")
	private Integer nope;
	
	@OneToMany(mappedBy = "freePosting", cascade = CascadeType.REMOVE)
	private List<FreeComment> freeCommentList;
	
	@ManyToOne
	private 

	@Override
	public String toString() {
		return "FreePosting [no=" + no + ", subject=" + subject + ", content=" + content + ", memberid=" + memberid
				+ ", membernick=" + membernick + ", createDate=" + createDate + ", updateDate=" + updateDate + ", cnt="
				+ cnt + ", ddabong=" + ddabong + ", nope=" + nope + ", freeCommentList=" + freeCommentList.isEmpty() + "]";
	}
	
	
	
}
