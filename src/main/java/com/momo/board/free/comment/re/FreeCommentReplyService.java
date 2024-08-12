package com.momo.board.free.comment.re;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.momo.board.free.comment.FreeComment;
import com.momo.board.free.comment.FreeCommentRepository;
import com.momo.member.Member;
import com.momo.member.MemberService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FreeCommentReplyService {

	private final FreeCommentReplyRepository freeCommentReplyRepository;
	private final FreeCommentRepository freeCommentRepository;
	private final MemberService memberService;
	
	public FreeCommentReply getCommentReply(Integer no) {
		Optional<FreeCommentReply> temp = freeCommentReplyRepository.findById(no);
		FreeCommentReply freeCommentReply = temp.get();
		return freeCommentReply;
	}
	
	public void create(Integer no, Member member, String content) {
		FreeComment freeComment = freeCommentRepository.findById(no).get();
		
		FreeCommentReply freeCommentReply = new FreeCommentReply();
		freeCommentReply.setContent(content);
		freeCommentReply.setAuthor(member);
		freeCommentReply.setMembernick(member.getMembernick());
		freeCommentReply.setCreateDate(LocalDateTime.now());
		freeCommentReply.setFreeComment(freeComment);
		
		freeCommentReplyRepository.save(freeCommentReply);
	}
	
	public void delete(Integer no) {
		freeCommentReplyRepository.deleteById(no);
	}
	
	public void update(FreeCommentReply freeCommentReply, String content) {
		freeCommentReply.setContent(content);
		freeCommentReply.setUpdateDate(LocalDateTime.now());
		
		freeCommentReplyRepository.save(freeCommentReply);
	}
	
    public void ddabong(FreeCommentReply freeCommentReply, Member member) {
        freeCommentReply.getDdabong().add(member);
    	freeCommentReplyRepository.save(freeCommentReply);
    }
    
    public void nope(FreeCommentReply freeCommentReply, Member member) {
    	freeCommentReply.getNope().add(member);
    	freeCommentReplyRepository.save(freeCommentReply);
    }
	
}
