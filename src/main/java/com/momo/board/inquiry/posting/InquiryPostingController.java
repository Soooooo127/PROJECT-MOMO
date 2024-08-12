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
	@GetMapping
	public String getMyPosting(Model model, Principal principal, InquiryPostingForm inquiryPostingForm, 
			                   @RequestParam(value="page", defaultValue = "0")int page) {
		Member member = this.memberService.getMember(principal.getName());
		Page<InquiryPosting> paging = this.inquiryPostingService.getMyList(member, page);
		model.addAttribute("paging", paging);
		return "/inquiry/mypage";
	}
	
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/create")
	public String createPosting(@Valid InquiryPostingForm inquiryPostingForm, BindingResult bindingResult, Principal principal, Model model
		                       , @RequestParam(value="page", defaultValue = "0")int page) {
		Member member = this.memberService.getMember(principal.getName());
		Page<InquiryPosting> paging = this.inquiryPostingService.getMyList(member, page);
		if(bindingResult.hasErrors()) {
			model.addAttribute("paging",paging);
			return "/inquiry/mypage";
		}
		this.inquiryPostingService.createPosting(inquiryPostingForm.getSubject(), inquiryPostingForm.getContent(), member.getMembernick() , member);
		return "redirect:/mypage/inquiryPosting";
	}
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/detail/{id}")
	public String detailPosting(Model model, @PathVariable(value="id") Integer id, Principal principal, 
			                    InquiryCommentForm inquiryCommentForm, InquiryPostingForm inquiryPostingForm) {
		InquiryPosting posting = this.inquiryPostingService.getInquiryPosting(id);
		model.addAttribute("posting", posting);
		return "/inquiry/inquiryPosting_detail";
	}
	
	@GetMapping("/start")
	public String home() {
		return "/inquiry/start";
	}
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/update/{id}")
	public String updatePosting(InquiryPostingForm inquiryPostingForm , @PathVariable(value="id")Integer id, Model model) {
		InquiryPosting inquiryPosting = this.inquiryPostingService.getInquiryPosting(id);
		
		inquiryPostingForm.setSubject(inquiryPosting.getSubject());
		inquiryPostingForm.setContent(inquiryPosting.getContent());
		
		model.addAttribute("inquiryPosting", inquiryPosting);
		
		return "/inquiry/inquiryPosting_form";
	}
	
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/update/{id}")
	public String updatePosting(@Valid InquiryPostingForm inquiryPostingForm, BindingResult bindingResult,
			                    @PathVariable(value="id")Integer id) {
		if(bindingResult.hasErrors()) {
			return "/inquiry/inquiryPosting_form";
		}
		this.inquiryPostingService.updatePosting(inquiryPostingForm.getSubject(), inquiryPostingForm.getContent(), id);
		return "redirect:/mypage/inquiryPosting/detail/{id}";
		
		
	}
	
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/delete/{id}")
	public String deletePosting(@PathVariable(value="id")Integer id, Principal principal) {
		this.inquiryPostingService.deletePosting(id);

		if(principal.getName().contains("test")) {
			return "redirect:/mypage/inquiryPosting/list";
		}
		return "redirect:/mypage/inquiryPosting";
		
	}
	
	

}
