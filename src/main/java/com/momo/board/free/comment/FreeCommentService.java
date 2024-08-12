package com.momo.board.free.comment;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.momo.board.free.posting.FreePosting;
import com.momo.board.free.posting.FreePostingRepository;
import com.momo.member.Member;
import com.momo.member.MemberService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FreeCommentService {
	
	private final FreeCommentRepository freeCommentRepository;
	private final FreePostingRepository freePostingRepository;
	private final MemberService memberService;
	
	public List<FreeComment> getList(Integer pno) {
		
		List<FreeComment> freeCommentList = new ArrayList<>();
		freeCommentList = freeCommentRepository.findAll();
		return freeCommentList;
	}
	
	public FreeComment getComment(Integer cno) {
		Optional<FreeComment> temp = freeCommentRepository.findById(cno);
		FreeComment freeComment = temp.get();
		return freeComment;
	}
	
	public void create(Integer pno, Member member, String content) {
		FreePosting freePosting = new FreePosting();
		Optional<FreePosting> findPosting = freePostingRepository.findById(pno);
		freePosting = findPosting.get();
		
		FreeComment freeComment = new FreeComment();
		freeComment.setFreePosting(freePosting);
		freeComment.setAuthor(member);
		freeComment.setContent(content);
		freeComment.setMembernick(member.getMembernick());
		freeComment.setCreateDate(LocalDateTime.now());
		
		freeCommentRepository.save(freeComment);
		
	}
	
	public void delete(Integer no) {
		freeCommentRepository.deleteById(no);
	}
	
	public void cntUp(Integer pno) {
		

	}

}
