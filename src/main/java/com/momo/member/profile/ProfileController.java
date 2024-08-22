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
		Member memberProfile = this.memberService.getMember(principal.getName());
		model.addAttribute("memberProfile", memberProfile);
		return "/profile/profile";
	}
	
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/modifyProfile")
	public String modifyProfile(ProfileForm profileForm, Model model) {
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
	
/*	@PreAuthorize("isAuthenticated()")
	@PostMapping("/modifyProfile")
	public String modifyProfile(@Valid ProfileForm profileForm, BindingResult bindingResult, 
			                    @RequestPart(value="files") MultipartFile file, Principal principal) throws IOException {
		//확장자 얻기
		String ext = this.imageService.extension(file.getOriginalFilename());
		
		if(bindingResult.hasErrors()) {
			return "/profile/profile_form";
		}
		
		//확장자 제한
	    if(!ext.contains("png") || !ext.contains("jpg") || !ext.contains("jfif")) {
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
	}  */
	
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/modifyProfile")
	public String modifyProfile(@Valid ProfileForm profileForm, BindingResult bindingResult, 
			                    @RequestPart(value="files") MultipartFile file, Principal principal) throws IOException {
		//확장자 얻기
		String ext = this.imageService.extension(file.getOriginalFilename());
		
		if(bindingResult.hasErrors()) {
			return "/profile/profile_form";
		}
		
		//확장자 제한
	 /*   if(!ext.contains("png") || !ext.contains("jpg") || !ext.contains("jfif")) {
			return "/profile/profile_form";
		}   */
		this.profileService.modifyProfile(principal.getName(), profileForm.getMembernick(), profileForm.getGender(), 
				                          profileForm.getContent(), profileForm.getMbti(), file);
		
		
		Member member = this.memberService.getMember(principal.getName());
		Image image = this.imageService.storeImage(file, member);
		Profile profile = this.profileService.getProfile(member);
			
		Image img = this.imageService.getImage(principal.getName());

		if(img == null) {
		  this.imageService.saveImage(image);
		} else {
		  this.imageService.updateImage(image, member);
		}
		
		this.memberService.updateMember(principal.getName(), profileForm.getMembernick(), profile , image);
		
		return "redirect:/mypage/profile";
	}
	


}
