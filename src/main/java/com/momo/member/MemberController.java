package com.momo.member;

import java.security.Principal;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
	
	private final MemberService memberService;
	private final PasswordEncoder passwordEncoder;
	
	// 첫페이지(인덱스)
	@GetMapping("/welcome")
	public String welcome() {
		return "index";
	}
	
	// 회원가입 : 약관 동의 화면
	@GetMapping("/signup")
	public String signup(MemberCreateForm memberCreateForm) {
		return "member/signup_cou";
	}
	
	// 회원가입 : 정보 입력 폼
	@GetMapping("/signup_next")
	public String signupNext(MemberCreateForm memberCreateForm) {
		return "member/signup_form";
	}
	
	// 회원가입을 위한 메소드
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
			memberService.create(memberCreateForm);
			
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
	
	// 로그인 화면으로 이동
	@GetMapping("/login")
	public String login() {
		return "member/login_form";
	}
	
	// 로그인 실패
	@GetMapping("/loginfailed")
	public String loginFailed(MemberCreateForm memberCreateForm) {
		return "redirect:member/login_form";
	}
	
	// 아이디 찾기
	@GetMapping("/findid")
	public String findId() {
		return "member/find_id3";
	}
	
	// 비밀번호 찾기
	@GetMapping("/findpw")
	public String findpw() {
		return "member/find_pw";
	}
	
//	// 마이페이지 진입
//	@GetMapping("/mypage")
//	public String goToMypage() {
//		return "profile/profile";
//	}
	
	// 회원정보 수정 메뉴 진입(초기화면 : 기본 정보를 보여준다)
	@GetMapping("/modifyMember")
	public String goToModify(Principal principal, Member member, Model model) {
		member = memberService.getMember(principal.getName());
		
		String _aid = member.getMemberid();
		String _aname = member.getMembername();
		String _amail = member.getEmail();
		
		int _mailAt = _amail.indexOf("@");
		int _mailShow = _amail.length() - _mailAt;
		
		_aid = _aid.substring(0, 2) + "*".repeat(_aid.length()-3) + _aid.substring(_aid.length()-1);
		_aname = _aname.substring(0, 1) + "*".repeat(_aname.length()-2) + _aname.substring(_aname.length()-1);
		_amail = _amail.substring(0, 2) + "*".repeat(_mailShow-2) + _amail.substring(_mailAt-1, _amail.length());

		member.setMemberid(_aid);
		member.setMembername(_aname);
		member.setEmail(_amail);

		model.addAttribute("member", member);
		
//		if(member.getCreateDate() == null) {
//			return "mypage/mypage_check_social";
//		} else {
//			return "mypage/mypage_check";
//		}
		return "mypage/mypage_check";
	}
	
	// 회원정보 수정 메소드
	@PostMapping("/modifyMember")
	public String modifyMember(@RequestParam(value = "memberid") String memberid, @RequestParam(value = "membernick") String membernick
			, @RequestParam(value = "email") String email, Member member, Model model) {
		System.out.println("=================회원정보 수정 메뉴 진입============");
		System.out.println("memberid : " + memberid);
		System.out.println("membernick : " + membernick);
		System.out.println("email : " + email);
		
		member = memberService.getMember(memberid);
		member.setMembernick(membernick);
		member.setEmail(email);
		
		memberService.updateMember(member);
		
		model.addAttribute("member", member);
		
		return "redirect:/member/modifyMember";
	}
	
	// 소셜 로그인 회원이 회원정보 첫 수정을 안했을 때 이동하는 페이지
	@GetMapping("/mypage/social")
	public String goToMypageSocial() {
		return "mypage/mypage_social";
	}
	
	
	// 마이페이지의 회원정보 수정을 진입하기 위한 비밀번호 체크 메소드
	@PostMapping("/checkPw")
	public String checkPw(@RequestParam(value = "password") String password, Principal principal, Model model) {
		Member member = memberService.getMember(principal.getName());
		boolean result = memberService.checkPassword(member, password);
		
		if(result) {
			model.addAttribute("member", member);
			return "mypage/mypage_modify";
		} else {
			return "/";
		}
		
	}
	
	
	// 아래부터는 테스트용 메소드들입니다!!!!!!!!!!!!!!!!!!!!!!!
	
	/*
	// 로그인 성공 시 이동 페이지(세션 저장용, 테스트)
	@GetMapping("/loginsuccessful")
	public String loginSuccessful(MemberCreateForm memberCreateForm, Principal principal,
			HttpServletRequest request, HttpSession session) {
		
		session = request.getSession();
		Member member = memberService.getMember(principal.getName());
		
		session.setAttribute("member", member);
		return "redirect:/";
	}
	*/

	
	@PostMapping("/friend")
	public String makeFriends(@RequestParam(value = "friendid") String friendid, Principal principal) {
		
		System.out.println("friendController에 진입하였습니다");
		Member friendMember = memberService.getMember(friendid);
		memberService.makeFriends(principal.getName(), friendMember);
		
		return "member/make_friends";
	}


	@GetMapping("/test")
	public String test() {
		
		System.out.println("불러오기 테스트");
		
		return "member/friend_test";
		
//		Member member = memberService.getMember(58);
		
		
	}
	
	@GetMapping("/mypageTest")
	public String goToMypageTest(Principal principal) {
		System.out.println("=================테스트 페이지 진입=================");
		System.out.println("principal username : " + principal.getName());
		return "mypage/mypage_test";
	}
	/*
	//친구 목록
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/friend")
	public String mypage(Principal principal, Model model) {
		
		System.out.println("친구목록 컨트롤러 진입");
		 Member member = this.memberService.getMember(principal.getName());
		 model.addAttribute("member", member);
		
		return "inquiry/mypage";
	}
	
	*/
	
	
	
	
	
}
