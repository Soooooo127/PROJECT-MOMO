package com.momo.board.free.posting;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FreePostingRepository extends JpaRepository<FreePosting, Integer> {

	Page<FreePosting> findAll(Pageable pageable);

	
}
