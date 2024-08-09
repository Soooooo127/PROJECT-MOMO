package com.momo.board.faq.posting;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.momo.board.faq.category.FaqCategory;

public interface FaqPostingRepository extends JpaRepository<FaqPosting, Integer>{

	List<FaqPosting> findByFaqCategory(FaqCategory faqCategory);
	
}
