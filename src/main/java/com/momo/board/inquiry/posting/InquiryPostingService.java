package com.momo.board.inquiry.posting;

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
import com.momo.board.inquiry.comment.InquiryComment;
import com.momo.member.Member;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InquiryPostingService {

	private final InquiryPostingRepository inquiryPostingRepository;
	
	
	public Page<InquiryPosting> getMyList(Member member, int page) {
		List<Sort.Order> sorts = new ArrayList<>();
		sorts.add(Sort.Order.desc(("createDate")));
		Pageable pageable = PageRequest.of(page,10, Sort.by(sorts));
		return this.inquiryPostingRepository.findByAuthor(member, pageable);
		
	}
	
	public Page<InquiryPosting> getList(int page){
		List<Sort.Order> sorts = new ArrayList<>();
		sorts.add(Sort.Order.desc("createDate"));
		Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
		return this.inquiryPostingRepository.findAll(pageable);
	}
	
	public Page<InquiryPosting> getNoCommentList(int page, List<InquiryComment> commentList){
		List<Sort.Order> sorts = new ArrayList<>();
		sorts.add(Sort.Order.desc("createDate"));
		Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
		return this.inquiryPostingRepository.findByCommentList(pageable, null);

	}
	
	public InquiryPosting getInquiryPosting(Integer no) {
		Optional<InquiryPosting> posting = this.inquiryPostingRepository.findById(no);
		if(posting.isPresent()) {
		return posting.get();
		} else {
			throw new DataNotFoundException("데이터가 없습니다");
		}
	}
	
	public void createPosting(String subject, String content, String membernick,Member member) {
		InquiryPosting inquiryPosting = new InquiryPosting();
		inquiryPosting.setSubject(subject);
		inquiryPosting.setContent(content);
		inquiryPosting.setCreateDate(LocalDateTime.now());
		inquiryPosting.setMembernick(membernick);
		inquiryPosting.setAuthor(member);
		this.inquiryPostingRepository.save(inquiryPosting);
	}
	
	public void updatePosting(String subject, String content, Integer no) {
		Optional<InquiryPosting> posting = this.inquiryPostingRepository.findById(no);
		if(posting.isPresent()) {
	    InquiryPosting post = posting.get();
	    post.setSubject(subject);
	    post.setContent(content);
	    post.setMembernick(post.getMembernick());
	    post.setUpdateDate(LocalDateTime.now());
	    this.inquiryPostingRepository.save(post);
		} else {
			throw new DataNotFoundException("데이터가 없습니다");
		}
		
	}
	
	public void deletePosting(Integer no) {
		this.inquiryPostingRepository.deleteById(no);
	}
}
