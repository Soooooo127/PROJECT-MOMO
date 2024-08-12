package com.momo.board.inquiry.posting;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.momo.user.SiteUser.SiteUser;
import java.util.List;


public interface InquiryPostingRepository extends JpaRepository<InquiryPosting, Integer> {
	
	Page<InquiryPosting> findAll (Pageable pageable);
	
    Page<InquiryPosting> findByAuthor(SiteUser author, Pageable pageable);
}
