package com.momo.board.inquiry.posting;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.momo.member.Member;


public interface InquiryPostingRepository extends JpaRepository<InquiryPosting, Integer> {
	
	Page<InquiryPosting> findAll (Pageable pageable);
	
    Page<InquiryPosting> findByAuthor(Member member, Pageable pageable);
}
