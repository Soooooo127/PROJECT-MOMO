package com.momo.restaurant.review;

import java.security.Principal;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.momo.board.free.comment.FreeComment;
import com.momo.board.free.comment.FreeCommentForm;
import com.momo.member.Member;
import com.momo.member.MemberService;
import com.momo.restaurant.RestService;
import com.momo.restaurant.Restaurant;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/rest/review")
public class ReviewController {

	private final ReviewService reviewService;
	private final RestService restService;
	private final MemberService memberService;
	
	@GetMapping("/list/{no}")
	private String getList(@PathVariable("no")Integer no) {
		return null;
	}
	
	
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/create/{no}")
	public String create(Model model
			, @PathVariable("no") Integer no, Principal principal
			,@Valid ReviewForm reviewForm, BindingResult bindingResult) {
		
		Restaurant rest = restService.getRestaurant(no);
		
		if(bindingResult.hasErrors()) {
			model.addAttribute("rest", rest);
			return "/rest/rest_detail";
		}
		Member member = memberService.getMember(principal.getName());
		reviewService.create(no, member, reviewForm.getContent());
		
		return "redirect:/rest/detail/{no}";
	}
	
	@GetMapping("/delete/{no}/{rno}")
	public String delete(@PathVariable("rno") Integer rno) {
		reviewService.delete(rno);
		return "redirect:/rest/detail/{no}";
	}
	
	
	@GetMapping("/update/{no}/{rno}")
	public String update(ReviewForm reviewForm,
			@PathVariable("no") Integer no, @PathVariable("rno") Integer rno) {
		Review review = reviewService.getReview(rno);
		reviewForm.setContent(review.getContent());
		
		return "rest/_comment_form";
	}
	
	
}
