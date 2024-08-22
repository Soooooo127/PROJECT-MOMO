package com.momo.member;

import java.security.Principal;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
	
	private final MemberService memberService;
	
	@GetMapping("/welcome")
	public String welcome() {
		return "index";
	}
	
	@GetMapping("/signup")
	public String signup(MemberCreateForm memberCreateForm) {
		return "member/signup_form";
	}
	
	@GetMapping("/loginfailed")
	public String loginFailed(MemberCreateForm memberCreateForm) {
		return "member/login_form";
	}
	
	/*
	@GetMapping("/loginsuccessful")
	public String loginSuccessful(MemberCreateForm memberCreateForm, Principal principal,
			HttpServletRequest request, HttpSession session) {
		
		session = request.getSession();
		Member member = memberService.getMember(principal.getName());
		
		session.setAttribute("member", member);
		return "redirect:/";
	}
	*/

	@PostMapping("/signup")
	public String signup(@Valid MemberCreateForm memberCreateForm, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return "member/signup_form";
		}

		if (!memberCreateForm.getPassword1().equals(memberCreateForm.getPassword2())) {
			bindingResult.rejectValue("password2", "passwordInCorrect", "2개의 패스워드가 일치하지 않습니다.");
			return "member/signup_form";
		}

		try {
			memberService.create(memberCreateForm.getMemberid(), memberCreateForm.getPassword1(), memberCreateForm.getMembernick(), memberCreateForm.getEmail());
		} catch (DataIntegrityViolationException e) {
			e.printStackTrace();
			bindingResult.reject("signupFailed", "이미 등록된 사용자입니다.");
			return "member/signup_form";
		} catch (Exception e) {
			e.printStackTrace();
			bindingResult.reject("signupFailed", e.getMessage());
			return "member/signup_form";
		}

		return "redirect:/";
	}

	@GetMapping("/login")
	public String login() {
		return "member/login_form";
	}
	
	@PostMapping("/friend")
	public String makeFriends(@RequestParam(value = "friendid") String friendid, Principal principal) {
		
		System.out.println("friendController에 진입하였습니다");
		Member friendMember = memberService.getMember(friendid);
		memberService.makeFriends(principal.getName(), friendMember);
		
		return "member/make_friends";
	}
	
	@GetMapping("/findid")
	public String findId() {
		return "member/find_id";
	}
	
	@PostMapping("/findid")
	public void findIds() {
		
	}
	
	@GetMapping("/test")
	public String test() {
		
		
		
		return "member/mail_test";
	}
	
	
	//친구 목록
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/friend")
	public String mypage(Principal principal, Model model) {
		
		System.out.println("친구목록 컨트롤러 진입");
		 Member member = this.memberService.getMember(principal.getName());
		 model.addAttribute("member", member);
		
		return "inquiry/mypage";
	}
	
	
	
	
	
	
	
}
