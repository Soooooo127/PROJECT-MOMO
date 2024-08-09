package com.momo.board.ask.comment;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.momo.DataNotFoundException;
import com.momo.board.ask.posting.AskPosting;
import com.momo.member.MomoMember;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AskCommentService {

	private final AskCommentRepository askCommentRepository;
	
	//답변작성 서비스구문
	public void create(AskPosting askPosting , String content , MomoMember membernick) {
		AskComment ac = new AskComment();
		ac.setContent(content);
		ac.setCreateDate(LocalDateTime.now());
		ac.setAskPosting(askPosting);
		ac.setMembernick(membernick);
		this.askCommentRepository.save(ac);
	}
	
	//답변수정 서비스
	public void update(AskComment askComment , String content) {
		askComment.setContent(content);
		askComment.setUpdateDate(LocalDateTime.now());
		this.askCommentRepository.save(askComment);
	}
	
	//답변삭제 서비스
	public void delete(AskComment askComment) {
		this.askCommentRepository.delete(askComment);
	}
	
	//답변조회
	public AskComment getAskComment(Integer no) {
		Optional<AskComment> askComment = this.askCommentRepository.findById(no);
		if(askComment.isPresent()) {
			
			return askComment.get();
		}else {
			throw new DataNotFoundException("No Data");
		}
	}
	
	//답변 페이지화 서비스구문
	public Page<AskComment> askCommentPage(AskPosting askPosting , int page){
		List<Sort.Order> sorts = new ArrayList<>();
		sorts.add(Sort.Order.desc("createDate"));
		
		Pageable pageable = PageRequest.of(page, 5);
		return askCommentRepository.findAllByAskPosting(askPosting, pageable);
	}
	
	//답변추천 서비스구문
	public void voteDdabong(AskComment askComment , MomoMember momoMember) {
		askComment.getDdabong().add(momoMember);
		this.askCommentRepository.save(askComment);
	}
	
	//답변비추천 서비스구문
	public void voteNope(AskComment askComment , MomoMember momoMember) {
		askComment.getNope().add(momoMember);
		this.askCommentRepository.save(askComment);
	}
}
