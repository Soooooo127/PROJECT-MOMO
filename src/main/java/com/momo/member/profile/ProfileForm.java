package com.momo.member.profile;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfileForm {

	
	@NotEmpty(message = "다시")
	@Column(unique = true)
	private String membernick;
	
	@NotEmpty(message = "다시")
	private String gender;
	
	@NotEmpty(message = "다시")
	private String mbti;
	
	@NotEmpty(message = "다시")
	private String content;
	
}
