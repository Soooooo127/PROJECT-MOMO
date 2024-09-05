package com.momo.restaurant.et;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.momo.member.Member;
import com.momo.restaurant.Restaurant;

public interface EatTogetherRepository extends JpaRepository<EatTogether, Integer>{

	@Query("select et from EatTogether et where et.rest = :rest order by et.regdate desc")
	List<EatTogether> findByRestOrderByDesc(@Param("rest") Restaurant rest);
	
	Page<EatTogether> findAll(Specification<EatTogether> spec , Pageable pageable);
	
	@Query("select et from EatTogether et where et.applymember=:applymember and et.ettitle like %:ettitle%")
	Page<EatTogether> findByAuthorAndEttitle(@Param(value="applymember")Member author, @Param(value="ettitle")String ettitle, Pageable pageable);
	
   
}
