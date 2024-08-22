package com.momo.member;

import java.time.LocalDateTime;
import java.util.List;

import com.momo.member.profile.Profile;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class Member {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer no;
	
	@Column(unique = true)
	private String memberid;
	
	@Column(unique = true)
	private String membernick;
	
	private String membername;
	
	private String password;
	
	@Column(unique = true)
	private String email;
	
	private LocalDateTime createDate;
	
	private String provider;
	
	private String providerId;
	
	@ManyToMany
	private List<Member> friend;
	
	@OneToOne
	@JoinColumn(name = "profile_id")
	private Profile profile;
	
	
	

}
