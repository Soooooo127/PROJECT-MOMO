package com.momo.board.free.comment.re;

import java.security.Principal;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.momo.member.Member;
import com.momo.member.MemberService;

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
		
		Member member = memberService.getUser(principal.getName());
		freeCommentReplyService.create(cno, member, freeCommentReplyForm.getContent());
		return "redirect:/free/detail/{pno}";
		
	}
	
	@GetMapping("/delete/{pno}/{rno}")
	public String delete(@PathVariable("rno") Integer rno) {
		
		System.out.println("진입하였습니다");
		freeCommentReplyService.delete(rno);
		
		return "redirect:/free/detail/{pno}";
	}
	
	
	
}
