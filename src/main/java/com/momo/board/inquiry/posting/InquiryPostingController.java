package com.momo.board.inquiry.posting;

import java.security.Principal;
import java.util.List;

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
import com.momo.user.SiteUser.SiteUser;
import com.momo.user.SiteUser.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/inquiryPosting")
public class InquiryPostingController {

	
	private final InquiryPostingService inquiryPostingService;
	private final UserService userService;
	
	@GetMapping("/list")
	public String getPostingList(Model model, @RequestParam(value="page", defaultValue = "0")int page) {
		Page<InquiryPosting> paging = this.inquiryPostingService.getList(page);
		model.addAttribute("paging", paging);
		return "/inquiry/inquiryPosting_list";
	}
	
	@GetMapping("/create")
	public String createPosting(InquiryPostingForm inquiryPostingForm) {
		return "/inquiry/mypage";
	}
	@PostMapping("/create")
	public String createPosting(@Valid InquiryPostingForm inquiryPostingForm, BindingResult bindingResult, Principal principal) {
		if(bindingResult.hasErrors()) {
			return "redirect:/user/mypage";
		}
		SiteUser siteUser = this.userService.getUser(principal.getName());
		this.inquiryPostingService.createPosting(inquiryPostingForm.getSubject(), inquiryPostingForm.getContent(), siteUser);
		return "redirect:/user/mypage";
	}
	
	@GetMapping("/detail/{id}")
	public String detailPosting(Model model, @PathVariable(value="id") Integer id, 
			                    InquiryCommentForm inquiryCommentForm, InquiryPostingForm inquiryPostingForm) {
		InquiryPosting posting = this.inquiryPostingService.getInquiryPosting(id);
		model.addAttribute("posting", posting);
		return "/inquiry/inquiryPosting_detail";
	}
	
	@GetMapping("/start")
	public String home() {
		return "/inquiry/start";
	}
	
	
	@GetMapping("/update/{id}")
	public String updatePosting(InquiryPostingForm inquiryPostingForm, Model model , @PathVariable(value="id")Integer id) {
		InquiryPosting inquiryPosting = this.inquiryPostingService.getInquiryPosting(id);
		
		inquiryPostingForm.setSubject(inquiryPosting.getSubject());
		inquiryPostingForm.setContent(inquiryPosting.getContent());
		
		model.addAttribute("inquiryPosting", inquiryPosting );
		
		return "/inquiry/inquiryPosting_form";
	}
	
	@PostMapping("/update/{id}")
	public String updatePosting(@Valid InquiryPostingForm inquiryPostingForm, BindingResult bindingResult,
			                    @PathVariable(value="id")Integer id) {
		if(bindingResult.hasErrors()) {
			return "/inquiry/inquiryPosting_form";
		}
		this.inquiryPostingService.updatePosting(inquiryPostingForm.getSubject(), inquiryPostingForm.getContent(), id);
		return "redirect:/inquiryPosting/detail/{id}";
		
		
	}
	
	

}
