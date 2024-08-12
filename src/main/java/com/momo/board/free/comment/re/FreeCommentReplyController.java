package com.momo.board.free.comment.re;

import java.security.Principal;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.momo.board.free.comment.FreeComment;
import com.momo.board.free.comment.FreeCommentForm;
import com.momo.member.Member;
import com.momo.member.MemberService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/free/comment/reply/")
public class FreeCommentReplyController {

	private final FreeCommentReplyService freeCommentReplyService;
	private final MemberService memberService;
	
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/create/{pno}/{cno}")
	public String create(@PathVariable("pno") Integer pno, @PathVariable("cno") Integer cno, 
			FreeCommentReplyForm freeCommentReplyForm, BindingResult bindingResult, Principal principal) {
		
		if(bindingResult.hasErrors()) {
			return "/free/detail/{pno}";
		}
		System.out.println("게시물 번호: " + pno);
		
		Member member = memberService.getMember(principal.getName());
		freeCommentReplyService.create(cno, member, freeCommentReplyForm.getContent());
		return "redirect:/free/detail/{pno}";
		
	}
	
	@GetMapping("/update/{pno}/{rno}")
	public String update(FreeCommentForm freeCommentForm,
			@PathVariable("pno") Integer pno, @PathVariable("rno") Integer rno) {
		FreeCommentReply freeCommentReply = freeCommentReplyService.getCommentReply(rno);
		
		freeCommentForm.setContent(freeCommentReply.getContent());
		
		return "free/free_comment_form";
	}
	
	@PostMapping("/update/{pno}/{rno}")
	public String update(@Valid FreeCommentForm freeCommentForm, BindingResult bindingResult,
			@PathVariable("pno") Integer pno, @PathVariable("rno") Integer rno) {
		
		if(bindingResult.hasErrors()) {
			return "redirect:/free/detail/{pno}";
		}
		
		FreeCommentReply freeCommentReply = freeCommentReplyService.getCommentReply(rno);
		freeCommentReplyService.update(freeCommentReply, freeCommentForm.getContent());
		
		return "redirect:/free/detail/{pno}";
	}
	
	
	@GetMapping("/delete/{pno}/{rno}")
	public String delete(@PathVariable("rno") Integer rno) {
		
		System.out.println("진입하였습니다");
		freeCommentReplyService.delete(rno);
		
		return "redirect:/free/detail/{pno}";
	}
	
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/ddabong/{pno}/{rno}")
    public String ddabong(Principal principal, @PathVariable("pno") Integer pno, @PathVariable("rno") Integer rno) {
        FreeCommentReply freeCommentReply = freeCommentReplyService.getCommentReply(rno);
        Member member = memberService.getMember(principal.getName());
        freeCommentReplyService.ddabong(freeCommentReply, member);
        return "redirect:/free/detail/{pno}";
    }
    
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/nope/{pno}/{rno}")
    public String nope(Principal principal, @PathVariable("pno") Integer pno, @PathVariable("rno") Integer rno) {
    	FreeCommentReply freeCommentReply = freeCommentReplyService.getCommentReply(rno);
    	Member member = memberService.getMember(principal.getName());
    	freeCommentReplyService.nope(freeCommentReply, member);
    	return "redirect:/free/detail/{pno}";
    }
	
}
