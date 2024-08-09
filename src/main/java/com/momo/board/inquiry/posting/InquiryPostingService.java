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

import com.momo.user.SiteUser.SiteUser;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InquiryPostingService {

	private final InquiryPostingRepository inquiryPostingRepository;
	
	
	public List<InquiryPosting> getMyList(SiteUser siteUser) {
		List<InquiryPosting> myList = new ArrayList<>();
		myList = this.inquiryPostingRepository.findByAuthor(siteUser);
		return myList;
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
	
	public void createPosting(String subject, String content, SiteUser siteUser) {
		InquiryPosting newQue = new InquiryPosting();
		newQue.setSubject(subject);
		newQue.setContent(content);
		newQue.setCreateDate(LocalDateTime.now());
		newQue.setAuthor(siteUser);
		this.inquiryPostingRepository.save(newQue);
	}
	
	public void updatePosting(String subject, String content, Integer id) {
		Optional<InquiryPosting> posting = this.inquiryPostingRepository.findById(id);
		if(posting.isEmpty()) {
             
		}
	    InquiryPosting post = posting.get();
	    post.setSubject(subject);
	    post.setContent(content);
	    this.inquiryPostingRepository.save(post);
		
	}
}
