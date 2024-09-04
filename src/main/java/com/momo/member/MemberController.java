package com.momo.member;

import java.security.Principal;
import java.time.LocalDateTime;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.momo.member.profile.Profile;
import com.momo.member.profile.ProfileService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
	
	private final MemberService memberService;
	private final PasswordEncoder passwordEncoder;
	private final AuthenticationManager authenticationManager;
	
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
	
	// 로그인 성공
	@GetMapping("/loginsuccessful")
	public String loginSuccessful(HttpSession session, Principal principal) {
		System.out.println("===========로그인 성공============");
		
		Member member = memberService.getMember(principal.getName());
		session.setAttribute("member", member);

		return "redirect:/mypage/profile";
		
	}
	
	// 로그인 실패
	@GetMapping("/loginfailed")
	public String loginFailed(MemberCreateForm memberCreateForm) {
		return "redirect:/member/login";
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
		
		System.out.println("CreateDate : " + member.getCreateDate());

		model.addAttribute("member", member);
		
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
	@GetMapping("/social")
	public String goToMypageSocial(Principal principal, Model model) {
		Member member  = memberService.getMember(principal.getName());
		
		if(member.getCreateDate() != null) {
			return "rediret:/member/modifyMember";
		} else {
			model.addAttribute("member", member);
			return "mypage/mypage_social";
		}
	}
	
	// 소셜 로그인 회원의 회원정보 첫 수정을 위한 페이지
	@PostMapping("/social")
	public String modifySocial(@RequestParam(value = "memberid") String memberid, @RequestParam(value = "membername") String membername
			, @RequestParam(value = "membernick") String membernick, @RequestParam(value = "modifyPw3") String password
			, @RequestParam(value = "mail") String email, Principal principal, Model model) {
		System.out.println("=========소셜 회원의 회원정보 첫 수정 메소드 진입==========");
		System.out.println("email : " + email);
		System.out.println("memberid : " + memberid);
		System.out.println("nick : " + membernick);
		System.out.println("name : " + membername);
		
		
		Member member = memberService.getMemberByEmail(email);
		
		member.setMemberid(memberid);
		member.setMembername(membername);
		member.setMembernick(membernick);
		member.setPassword(passwordEncoder.encode(password));
		member.setCreateDate(LocalDateTime.now());
		
		memberService.updateMember(member);
		
		model.addAttribute("member", member);
		
	    Authentication authentication = authenticationManager.authenticate
	    		(new UsernamePasswordAuthenticationToken(memberid, password));
	        SecurityContextHolder.getContext().setAuthentication(authentication);

		return "redirect:/member/modifyMember";

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
	
	// 회원 탈퇴 페이지 이동
	@GetMapping("/drop")
	public String goToDrop() {
		return "member/drop_member";
	}
	
	// 회원 탈퇴 처리
	@PostMapping("/drop")
	public String goToDrop(@RequestParam(value = "memberid") String memberid, @RequestParam(value = "mail") String mail
			, @RequestParam(value = "password") String password, Principal principal) {
		
		System.out.println("========회원 탈퇴 메소드 진입=========");
		System.out.println("memberid : " + memberid);
		System.out.println("email : " + mail);
		System.out.println("password : " + password);

		
		Member _member = memberService.getMember(memberid);
		boolean pwCheckResult = false;
		
		if(principal.getName().equals(memberid)) {
			System.out.println("아이디가 같습니다.");
			pwCheckResult = memberService.checkPassword(_member, password);
			
			if(pwCheckResult) {
				System.out.println("비밀번호가 같습니다.");
				
				if(mail.equals(_member.getEmail())) {
					System.out.println("========모든 정보가 일치합니다=========");
					
					memberService.deleteMember(_member);
					
					return "redirect:/member/logout";

				} else {
					System.out.println("메일주소가 다릅니다");
					
				}
			} else {
				System.out.println("비밀번호가 같지 않습니다.");
				
			}
		} else {
			System.out.println("아이디가 같지 않습니다.");
			
		}
		
		return "redirect:/member/drop";
	}
	

	
	// 아래부터는 테스트용 메소드들입니다!!!!!!!!!!!!!!!!!!!!!!!

	
}
