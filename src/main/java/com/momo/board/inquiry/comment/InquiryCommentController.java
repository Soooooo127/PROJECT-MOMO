package com.momo.board.inquiry.comment;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.momo.board.inquiry.posting.InquiryPosting;
import com.momo.board.inquiry.posting.InquiryPostingService;
import com.momo.user.SiteUser.SiteUser;
import com.momo.user.SiteUser.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/inquiryComment")
public class InquiryCommentController {
	
	private final InquiryCommentService inquiryCommentService;
	private final InquiryPostingService inquiryPostingService;
	private final UserService userService;
	
	@PostMapping("/create/{id}")
	public String createAnswer(Model model, @PathVariable("id") Integer id, 
			     @Valid InquiryCommentForm inquiryCommentForm, BindingResult bindingResult, Principal principal) {
		InquiryPosting inquiryPosting = this.inquiryPostingService.getInquiryPosting(id);
		SiteUser siteUser = this.userService.getUser(principal.getName());
		
		if(bindingResult.hasErrors()) {
		   model.addAttribute("inquiryPosting", inquiryPosting);
		   return "/inquiry/inquiryPosting_detail";	
		}
		this.inquiryCommentService.create(inquiryPosting, inquiryCommentForm.getContent(), siteUser);
		return String.format("redirect:/inquiryPosting/detail/%s", id);
	}

}
