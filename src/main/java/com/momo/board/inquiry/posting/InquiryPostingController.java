package com.momo.board.inquiry.posting;

import java.security.Principal;

import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.momo.board.inquiry.comment.InquiryCommentForm;
import com.momo.member.Member;
import com.momo.member.MemberService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/mypage/inquiryPosting")
public class InquiryPostingController {

	
	private final InquiryPostingService inquiryPostingService;
	private final MemberService memberService;
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/list")
	public String getPostingList(Model model, @RequestParam(value="page", defaultValue = "0")int page) {
		Page<InquiryPosting> paging = this.inquiryPostingService.getList(page);
		model.addAttribute("paging", paging);
		return "/inquiry/inquiryPosting_list";
	}  
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/noCommentList")
	public String getNoCommentList(Model model, @RequestParam(value="page", defaultValue = "0")int page) {
		Page<InquiryPosting> paging = this.inquiryPostingService.getNoCommentList(page, null);
		model.addAttribute("paging", paging);
		return "/inquiry/inquiryPosting_list";
	}
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping
	public String getMyPosting(Model model, Principal principal, InquiryPostingForm inquiryPostingForm, 
			                   @RequestParam(value="page", defaultValue = "0")int page) {
		Member member = this.memberService.getMember(principal.getName());
		Page<InquiryPosting> paging = this.inquiryPostingService.getMyList(member, page);
		model.addAttribute("paging", paging);
		return "/inquiry/inquiryPosting";
	}
	
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/create")
	public String createPosting(@Valid InquiryPostingForm inquiryPostingForm, BindingResult bindingResult, Principal principal, Model model
		                       , @RequestParam(value="page", defaultValue = "0")int page) {
		Member member = this.memberService.getMember(principal.getName());
		Page<InquiryPosting> paging = this.inquiryPostingService.getMyList(member, page);
		if(bindingResult.hasErrors()) {
			model.addAttribute("paging",paging);
			return "/inquiry/inquiryPosting";
		}
		this.inquiryPostingService.createPosting(inquiryPostingForm.getSubject(), inquiryPostingForm.getContent(), member.getMembernick() , member);
		return "redirect:/mypage/inquiryPosting";
	}
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/detail/{no}")
	public String detailPosting(Model model, @PathVariable(value="no") Integer no, Principal principal, 
			                    InquiryCommentForm inquiryCommentForm, InquiryPostingForm inquiryPostingForm) {
		InquiryPosting posting = this.inquiryPostingService.getInquiryPosting(no);
		model.addAttribute("posting", posting);
		return "/inquiry/inquiryPosting_detail";
	}
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/update/{no}")
	public String updatePosting(InquiryPostingForm inquiryPostingForm , @PathVariable(value="no")Integer no, Model model) {
		InquiryPosting posting = this.inquiryPostingService.getInquiryPosting(no);
		
		inquiryPostingForm.setSubject(posting.getSubject());
		inquiryPostingForm.setContent(posting.getContent());
		
		model.addAttribute("posting", posting);
		
		return "/inquiry/inquiryPosting_form";
	}
	
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/update/{no}")
	public String updatePosting(@Valid InquiryPostingForm inquiryPostingForm, BindingResult bindingResult,
			                    @PathVariable(value="no")Integer no, Model model) {
		InquiryPosting posting = this.inquiryPostingService.getInquiryPosting(no);
		if(bindingResult.hasErrors()) {
			model.addAttribute("posting", posting);
			return "/inquiry/inquiryPosting_form";
		}
		this.inquiryPostingService.updatePosting(inquiryPostingForm.getSubject(), inquiryPostingForm.getContent(), no);
		return "redirect:/mypage/inquiryPosting/detail/{no}";
		
		
	}
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/delete/{no}")
	public String deletePosting(@PathVariable(value="no")Integer no, Principal principal) {
		this.inquiryPostingService.deletePosting(no);

		if(principal.getName().contains("admin")) {
			return "redirect:/mypage/inquiryPosting/list";
		}
		return "redirect:/mypage/inquiryPosting";
		
	}
	

}
