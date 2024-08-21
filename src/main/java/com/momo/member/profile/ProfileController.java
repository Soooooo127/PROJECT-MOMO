 package com.momo.member.profile;

import java.io.IOException;
import java.security.Principal;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.momo.image.Image;
import com.momo.image.ImageService;
import com.momo.member.Member;
import com.momo.member.MemberService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/mypage")
public class ProfileController {

	private final ProfileService profileService;
	private final MemberService memberService;
	private final ImageService imageService;
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/profile")
	public String profile(Model model, Principal principal) {
		Image profile = this.imageService.getImage(principal.getName());
		model.addAttribute("profile", profile);
		return "/profile/profile";
	}
	
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/modifyProfile")
	public String modifyProfile(ProfileForm profileForm) {
		return "/profile/profile_form";
	}
	
	
	
/*	@PreAuthorize("isAuthenticated()")
	@PostMapping("/modifyProfile")
	public String modifyProfile(@Valid ProfileForm profileForm, BindingResult bindingResult, 
			                    @RequestParam MultipartFile file, Principal principal) throws IOException {
		
		Member member = this.memberService.getMember(principal.getName());
		
		if(bindingResult.hasErrors()) {
			return "/profile/form";
		}
		this.profileService.modifyProfile(member, profileForm.getMembernick(), profileForm.getGender() 
				                          , profileForm.getContent(), profileForm.getMbti(), file);
		
		
		
		return "redirect:/mypage/profile";
	}  */
	
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/modifyProfile")
	public String modifyProfile(@Valid ProfileForm profileForm, BindingResult bindingResult, 
			                    @RequestPart(value="files") MultipartFile file, Principal principal) throws IOException {
		
		if(bindingResult.hasErrors()) {
			return "/profile/profile_form";
		}
		this.profileService.modifyProfile(principal.getName(), profileForm.getMembernick(), profileForm.getGender(), 
				                          profileForm.getContent(), profileForm.getMbti(), file);
		
		
		Member member = this.memberService.getMember(principal.getName());
		Profile profile = this.profileService.getProfile(member);
		Image image = this.imageService.storeImage(file, profile);
		
		Image img = this.imageService.getImage(principal.getName());
		
		
		this.memberService.updateMember(principal.getName(), profileForm.getMembernick());
				
		if(img == null) {
		  this.imageService.saveImage(image);
		} else if(img != null) {
		  this.imageService.updateImage(image, profile);
		}
		
		
		
		return "redirect:/mypage/profile";
	}
	
	
	


}
