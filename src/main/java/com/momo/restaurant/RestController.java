package com.momo.restaurant;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.momo.member.Member;
import com.momo.member.MemberService;
import com.momo.restaurant.et.EatTogether;
import com.momo.restaurant.jjim.Jjim;
import com.momo.restaurant.jjim.JjimService;
import com.momo.restaurant.review.Review;
import com.momo.restaurant.review.ReviewForm;
import com.momo.restaurant.review.ReviewService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/rest")
public class RestController {

	private final RestService restService;
	private final MemberService memberService;
	private final JjimService jjimService;
	private final ReviewService reviewService;
	
	@GetMapping("/")
	public String restAll() {
		return "rest/rest_all";
	}
	
	//@ResponseBody JSON 으로 보내
	@GetMapping("/detail/{no}")
	public String restDetail(Model model, @PathVariable("no")Integer no
			, Principal principal, ReviewForm reviewForm) {
		if(principal==null) {
			Restaurant rest = this.restService.getRestaurant(no);
			model.addAttribute("rest", rest);
			List<Review> review=this.reviewService.getList(rest);
			model.addAttribute("review", review);
			List<Jjim> j = this.jjimService.getList(rest);
			model.addAttribute("j", j);
			return "rest/rest_detail";
		}
		
		
		Restaurant rest = this.restService.getRestaurant(no);
		model.addAttribute("rest", rest);
		
		List<EatTogether> etList = rest.getEtList();
		
		LocalDate today = LocalDate.now();
		Integer expired = 0;
		for(EatTogether et : etList) {
			if(today.isAfter(et.getEtdate().toLocalDate())) {
				expired += 1;
				
			}
		}
		rest.setProgresset(etList.size() - expired);
		
		
		
		
		Member member = this.memberService.getMember(principal.getName());
		Jjim jjim = this.jjimService.get(member, rest);
		model.addAttribute("jjim", jjim);

		List<Jjim> j = this.jjimService.getList(rest);
		model.addAttribute("j", j);
		
		List<Review> review=this.reviewService.getList(rest);
		model.addAttribute("review", review);
		
		
		
		return "rest/rest_detail";
	}
	
	
	@GetMapping("/list/{category}")
	public String restList(Model model, @PathVariable("category") String category
			, @RequestParam(value="page", defaultValue = "0") int page
			) {
		Page<Restaurant>paging = this.restService.getList(category, page);
		model.addAttribute("paging", paging);

		return "rest/rest_list";
	}
	
	
	
	
	@GetMapping("/list/search")
	public String restListSerch(Model model
			, @RequestParam(value = "kw", defaultValue = "") String kw
			, @RequestParam(value="page", defaultValue = "0") int page) {
		Page<Restaurant>paging = this.restService.getListSearch(kw, page);
		model.addAttribute("paging", paging);
		model.addAttribute("kw", kw);
		
		List<Restaurant> restList = this.restService.getList(kw);
		model.addAttribute("restList", restList);
		
		return "rest/rest_search";
	}
	
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/jjim/{no}")
	public String Jjim(Model model, Principal principal, @PathVariable("no") Integer no) {
		
		Restaurant rest = this.restService.getRestaurant(no);
        Member member = this.memberService.getMember(principal.getName());
		this.jjimService.create(member, rest);
        //this.restService.Jjim(rest, member);
		
		Jjim jjim = this.jjimService.get(member, rest);
		model.addAttribute("jjim", jjim);
        return "redirect:/rest/detail/{no}";
	}
	
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/nojjim/{no}")
	public String NoJjim(Model model, Principal principal, @PathVariable("no") Integer no) {
		Restaurant rest = this.restService.getRestaurant(no);
		Member member = this.memberService.getMember(principal.getName());
		//this.restService.NoJjim(rest, member);
		Jjim jjim = this.jjimService.get(member, rest);
		model.addAttribute("jjim", jjim);
		this.jjimService.delete(member, rest);
		return "redirect:/rest/detail/{no}";
	}  
	
	
	
	
	@GetMapping("/et")
	public String eat() {
		
		return "rest/eat";
	}

	
	
}
