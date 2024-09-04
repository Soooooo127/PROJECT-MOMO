package com.momo.board.free.comment.re;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

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
	
    public void ddabong(FreeCommentReply freeCommentReply, String memberid) {
    	
    	if(freeCommentReply.getDdabong().isEmpty()) {
    		Set<String> _ddabong = new HashSet<>();
    		_ddabong.add(memberid);
    		freeCommentReply.setDdabong(_ddabong);
    		
    	} else {
    		
    		if(freeCommentReply.getDdabong().contains(memberid)) {
    			freeCommentReply.getDdabong().remove(memberid);
    		} else {
    			freeCommentReply.getDdabong().add(memberid);
    		}
    	}
    	
    	freeCommentReplyRepository.save(freeCommentReply);
    }
    
    public void nope(FreeCommentReply freeCommentReply, String memberid) {
    	
    	if(freeCommentReply.getNope().isEmpty()) {
    		Set<String> _nope = new HashSet<>();
    		_nope.add(memberid);
    		freeCommentReply.setNope(_nope);
    		
    	} else {
    		if(freeCommentReply.getNope().contains(memberid)) {
    			freeCommentReply.getNope().remove(memberid);
    		} else {
    			freeCommentReply.getNope().add(memberid);
    		}
    	}
    	
    	freeCommentReplyRepository.save(freeCommentReply);
    }
	
}
