package com.momo.board.free.comment;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import com.momo.board.free.comment.re.FreeCommentReply;
import com.momo.board.free.posting.FreePosting;

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
public class FreeComment {

	// 댓글
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer no;
	
	@Column(columnDefinition = "TEXT")
	private String content;
	
	private String memberid;
	
	private String membernick;
	
	private LocalDateTime createDate;
	
	private LocalDateTime updateDate;

	@ColumnDefault("0")
	private Integer ddabong;
	
	@ColumnDefault("0")
	private Integer nope;
	
	@ManyToOne
	private FreePosting freePosting;
	
	@OneToMany(mappedBy = "freeComment", cascade = CascadeType.REMOVE)
	private List<FreeCommentReply> freeCommentReplyList;
	
	
}
