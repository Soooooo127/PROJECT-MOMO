package com.momo.board.free.comment;

import java.security.Principal;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.momo.board.free.posting.FreePosting;
import com.momo.board.free.posting.FreePostingService;
import com.momo.member.Member;
import com.momo.member.MemberService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/free/comment")
public class FreeCommentController {

	private final FreeCommentService freeCommentService;
	private final FreePostingService freePostingService;
	private final MemberService memberService;
	
	@GetMapping("/list/{pno}")
	public String getList(@PathVariable("pno") Integer pno) {
		return null;
	}
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/create/{pno}")
	public String create(Model model) {
		model.addAttribute("freeCommentForm", new FreeCommentForm());
		return "/free/free_list";
	}
	
	
	@PostMapping("/create/{pno}")
	public String create(Model model, @PathVariable("pno") Integer pno, @Valid FreeCommentForm freeCommentForm,
			BindingResult bindingResult, Principal principal) {
		FreePosting freePosting = freePostingService.getPosting(pno);
		
		if (bindingResult.hasErrors()) {
			model.addAttribute("freePosting", freePosting);
			return "/free/free_detail";
		}
		
		Member member = memberService.getUser(principal.getName());
		freeCommentService.create(pno, member, freeCommentForm.getContent());
		return String.format("redirect:/free/detail/%s", freePosting.getNo());
	}
	
	@GetMapping("/delete/{pno}/{cno}")
	public String delete(@PathVariable("cno") Integer cno) {
		freeCommentService.delete(cno);
		return "redirect:/free/detail/{pno}";
	}
	
	
	/*
	
	@PostMapping("/create/{pno}")
	public String create(@PathVariable("pno") Integer pno,
			@Valid FreeCommentForm freeCommentForm, BindingResult bindingResult) {
		System.out.println(pno + ", " + "content");
		
		if(bindingResult.hasErrors()) {
			
		}
		
		freeCommentService.create(pno, freeCommentForm.getContent());
		return "redirect:/free/detail/{pno}";
		
	}
	*/
	
	@GetMapping("/update/{pno}/{cno}")
	public String update(Model model, @PathVariable("pno") Integer pno, @PathVariable("cno") Integer cno) {
		FreeComment freeComment = freeCommentService.getComment(cno);
		model.addAttribute("freeComment", freeComment);
		
		return "free_detail :: #myContent";
	}

//	https://samori.tistory.com/56
	
	
	@PostMapping("/update/{pno}/{cno}")
	public String update(Model model) {
		return "/free/comment/update/:: #myContent";
	}
	
}
