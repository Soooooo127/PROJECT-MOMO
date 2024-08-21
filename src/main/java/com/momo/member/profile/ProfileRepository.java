package com.momo.member.profile;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.momo.member.Member;

public interface ProfileRepository extends JpaRepository<Profile, Integer> {
	
	Optional<Profile> findByAuthor(Member member);
	
}
