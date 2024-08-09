package com.momo.board.ask.comment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.momo.board.ask.posting.AskPosting;

public interface AskCommentRepository extends JpaRepository<AskComment, Integer>{

	Page<AskComment> findAllByAskPosting(AskPosting askPosting , Pageable pageable);
}
