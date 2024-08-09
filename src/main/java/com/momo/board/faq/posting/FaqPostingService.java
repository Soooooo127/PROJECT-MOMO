package com.momo.board.faq.posting;

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
import com.momo.board.faq.category.FaqCategory;
import com.momo.member.Member;
import com.momo.member.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FaqPostingService {

	private final FaqPostingRepository faqPostingRepository;

	
	public void createFaq( FaqCategory category, String subject, String content, Member author) {
		FaqPosting faq = new FaqPosting();
		faq.setFaqCategory(category);
		faq.setSubject(subject);
		faq.setContent(content);
		faq.setCreateDate(LocalDateTime.now());
	
		faq.setAuthor(author);
		
		this.faqPostingRepository.save(faq);
	}
	
	public Page<FaqPosting> getList(int page){
		List<Sort.Order> sorts = new ArrayList<>();
		sorts.add(Sort.Order.desc("createDate"));
		Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
			
		return this.faqPostingRepository.findAll(pageable);
	}
	
	
	public List<FaqPosting> getList1(FaqCategory faqCategory){
		return this.faqPostingRepository.findByFaqCategory(faqCategory);
	}
	
	
	public FaqPosting getfaqPosting(Integer no) {
		Optional<FaqPosting> faq = this.faqPostingRepository.findById(no);
		if(faq.isPresent()) {
			return faq.get();
		}else {
			throw new DataNotFoundException("데이터가 없습니다");
		}
		
	}
	
	public void delete(Integer no) {
		this.faqPostingRepository.deleteById(no);
	}
	

	public void update(Integer no, FaqCategory category, String subject, String content) {
		Optional<FaqPosting> posting=this.faqPostingRepository.findById(no);
		FaqPosting p = posting.get();
		p.setFaqCategory(category);
		p.setSubject(subject);
		p.setContent(content);
		p.setUpdateDate(LocalDateTime.now());
		
	}
	
}
