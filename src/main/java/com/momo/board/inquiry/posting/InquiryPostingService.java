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
	
	public InquiryPosting getInquiryPosting(Integer id) {
		Optional<InquiryPosting> posting = this.inquiryPostingRepository.findById(id);
		if(posting.isPresent()) {
		return posting.get();
		} else {
			return null;
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
	
	public void updatePosting(String subject, String content, Integer id) {
		Optional<InquiryPosting> posting = this.inquiryPostingRepository.findById(id);
		if(posting.isEmpty()) {
             
		}
	    InquiryPosting post = posting.get();
	    post.setSubject(subject);
	    post.setContent(content);
	    post.setMembernick(post.getMembernick());
	    post.setUpdateDate(LocalDateTime.now());
	    this.inquiryPostingRepository.save(post);
		
	}
	
	public void deletePosting(Integer id) {
		this.inquiryPostingRepository.deleteById(id);
	}
}
