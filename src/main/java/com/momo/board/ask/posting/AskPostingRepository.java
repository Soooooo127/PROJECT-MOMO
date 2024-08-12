package com.momo.board.ask.posting;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AskPostingRepository extends JpaRepository<AskPosting, Integer>{
	
	Page<AskPosting> findAll(Specification<AskPosting> spec , Pageable pageable);
}
