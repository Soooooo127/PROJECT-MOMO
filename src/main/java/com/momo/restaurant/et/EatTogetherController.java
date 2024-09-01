package com.momo.restaurant.et;

import java.security.Principal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import com.momo.member.Member;
import com.momo.member.MemberService;

import com.momo.restaurant.RestService;
import com.momo.restaurant.Restaurant;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/et")
public class EatTogetherController {

	private final EatTogetherService etService;
	private final MemberService momoMemberService;
	private final RestService restService;
	
	@GetMapping("/list")
	public String listET(Model model) {
		List<EatTogether> etList = this.etService.getListAll();
		model.addAttribute("etList", etList);
		
		
		
		return "et/et_list";
	}
	
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/create/{no}")
	public String createET(Model model , @PathVariable("no") Integer no 
			, Principal principal , @Valid EatTogetherForm eatTogetherForm , BindingResult bindingResult) {
		
		Restaurant rest = this.restService.getRestaurant(no);
		Member applymember = this.momoMemberService.getMember(principal.getName());
		model.addAttribute("rest" , rest);
		if(bindingResult.hasErrors()) {
			System.out.println("같이먹기 등록 실패");
			
			return "rest/rest_detail";
		}
		System.out.println("같이먹기 등록 데이터 입력전");
		
		this.etService.create(rest , applymember , eatTogetherForm.getEttitle() , eatTogetherForm.getEtdate()
				, eatTogetherForm.getPrtnumber() , eatTogetherForm.getPrefgender() , eatTogetherForm.getPrefmbti());
		
		
		return String.format("redirect:/rest/detail/%s", no);
	}
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/participate/{rno}/{etno}")
	public String partiET(@PathVariable("rno") Integer rno ,
		@PathVariable("etno") Integer etno , Principal principal
		, Model model) {
		
		EatTogether et = this.etService.getET(etno);
		Member momoMember = this.momoMemberService.getMember(principal.getName());
		int prtnumber = Integer.parseInt(et.getPrtnumber());
		if(prtnumber <= et.getPrtmember().size()) {
			System.out.println("===인원수를 초과하였습니다===");
			return String.format("redirect:/et/detail/%s/%d", rno , etno);
		}
		for(Member member : et.getPrtmember()) {
			if(member.getMemberid() == momoMember.getMemberid()) {
				System.out.println("===이미 참여한 방입니다===");
				return String.format("redirect:/et/detail/%s/%d", rno , etno);
			}
		}
		
		this.etService.participate(et, momoMember);
		System.out.println("===성공적으로 참여하였습니다===");
		return String.format("redirect:/et/detail/%s/%d", rno , etno);
	}
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/detail/{rno}/{etno}")
	public String detailET(Model model , @PathVariable("rno") Integer rno
			, @PathVariable("etno") Integer etno , Principal principal) {
		Restaurant rest = this.restService.getRestaurant(rno);
		EatTogether et = this.etService.getET(etno);
		
		List<Member> temp = et.getPrtmember();
		for(int i=0 ; i < temp.size() ; i++) {
			System.out.println(temp.get(i).getMembernick());
		}
		Member member = this.momoMemberService.getMember(principal.getName());
		boolean isMemberIn = temp.contains(member);
		System.out.println(isMemberIn);
		
		model.addAttribute("isMemberIn" , isMemberIn);
		model.addAttribute("rest", rest);
		model.addAttribute("et", et);
		
		return "et/et_detail";
	}
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/delete/{rno}/{etno}")
	public String deleteET(Principal principal , @PathVariable("rno") Integer rno , @PathVariable("etno") Integer etno) {
		EatTogether et = this.etService.getET(etno);
		Member momoMember = this.momoMemberService.getMember(principal.getName());
		if(!et.getApplymember().getMemberid().equals(momoMember.getMemberid())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST , "삭제 권한이 없습니다");
		}
		this.etService.delete(et);
		return String.format("redirect:/rest/detail/%s", rno);
	}
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/leave/{rno}/{etno}")
	public String leaveET(Principal principal, @PathVariable("rno") Integer rno
			, @PathVariable("etno") Integer etno , Model model) {
		Restaurant rest = this.restService.getRestaurant(rno);
		EatTogether et = this.etService.getET(etno);
		Member momoMember = this.momoMemberService.getMember(principal.getName());
		List<Member> prtmember = et.getPrtmember();
		for(Member member : prtmember) {
			if(member.getMemberid().equals(momoMember.getMemberid())) {
				this.etService.leave(et, momoMember);
				System.out.println("===방 나가기 성공===");
				return String.format("redirect:/et/detail/%s/%d", rno , etno);
			}
		}
		
		
		return String.format("redirect:/et/detail/%s/%d", rno , etno);
	}
}
