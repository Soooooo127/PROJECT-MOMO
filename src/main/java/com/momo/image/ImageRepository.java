package com.momo.image;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.momo.member.profile.Profile;

public interface ImageRepository extends JpaRepository<Image, Integer> {

	
	
	Optional<Image> findByProfile(Profile profile);
}
