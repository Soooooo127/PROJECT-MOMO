package com.momo.member.profile;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import com.momo.member.Member;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@DynamicInsert
@Setter
@Getter
@Entity
public class Profile {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer no;
	
	private String membernick;
	
	@ColumnDefault("4.5")
	private float brix;

	private String gender;
	
	private String mbti;
	
	private String content;

	@OneToOne(cascade = CascadeType.REMOVE)
    private Member author;
	

}
