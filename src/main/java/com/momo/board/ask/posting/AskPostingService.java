package com.momo.board.ask.posting;

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
import com.momo.member.MomoMember;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AskPostingService {
	private final AskPostingRepository askPostingRepository;
	//질문글 목록 페이지형식 서비스구문
	public Page<AskPosting> getList(int page){
		List<Sort.Order> sorts = new ArrayList<Sort.Order>();
		sorts.add(Sort.Order.desc("createDate"));
		Pageable pageable = PageRequest.of(page ,  10 , Sort.by(sorts));
		return this.askPostingRepository.findAll(pageable);
	}
	
	//질문글 조회 서비스구문
	public AskPosting getAskPosting(Integer no) {
		Optional<AskPosting> askPosting = this.askPostingRepository.findById(no);
		if(askPosting.isPresent()) {
			AskPosting ap = askPosting.get();
			ap.setCnt(ap.getCnt() + 1);
			return this.askPostingRepository.save(ap);
		}else {
			throw new DataNotFoundException("No Data");
		}
	}
	
	//질문글 작성 서비스구문
	public void create(String subject , String content , MomoMember membernick) {
		AskPosting ap = new AskPosting();
		ap.setSubject(subject);
		ap.setContent(content);
		ap.setMembernick(membernick);
		ap.setCreateDate(LocalDateTime.now());
		this.askPostingRepository.save(ap);
	}
	
	//질문글 수정 서비스구문
	public void update(AskPosting askPosting , String subject , String content) {
		askPosting.setSubject(subject);
		askPosting.setContent(content);
		askPosting.setUpdateDate(LocalDateTime.now());
		this.askPostingRepository.save(askPosting);
	}
	
	//질문글 삭제 서비스구문
	public void delete(AskPosting askPosting) {
		this.askPostingRepository.delete(askPosting);
	}
	
	//질문글 추천 서비스구문
	public void voteDdabong(AskPosting askPosting , MomoMember momoMember) {
		askPosting.getDdabong().add(momoMember);
		this.askPostingRepository.save(askPosting);
	}
	
	//질문글 비추천 서비스구문
	public void voteNope(AskPosting askPosting , MomoMember momoMember) {
		askPosting.getNope().add(momoMember);
		this.askPostingRepository.save(askPosting);
	}
	
	
}
