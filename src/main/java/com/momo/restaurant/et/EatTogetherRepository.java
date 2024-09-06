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
	Page<EatTogether> findAll(Pageable pageable);
	
	@Query("select et from EatTogether et where et.applymember=:applymember and et.ettitle like %:ettitle%")
	Page<EatTogether> findByAuthorAndEttitle(@Param(value="applymember")Member author, @Param(value="ettitle")String ettitle, Pageable pageable);
	
	//제목으로 검색하는 레파지토리 
	@Query("select et from EatTogether et where et.ettitle like %:kw% ")
	Page<EatTogether> findEttitleByKeyword(@Param("kw") String kw , Pageable pageable);
		
	//식당 이름을 검색하는 레파지토리
    @Query("select "
            + "distinct et "
            + "from EatTogether et " 
            + "left outer join Restaurant r on et.rest=r "
            + "where "
            + "   r.name like %:kw% ")
	Page<EatTogether> findNameByKeyword(@Param("kw") String kw , Pageable pageable);
    
    //선호 mbti 를 검색하는 레파지토리
    @Query("select et from EatTogether et where et.prefmbti like %:kw% ")
    Page<EatTogether> findPrefmbtiByKeyword(@Param("kw") String kw , Pageable pageable);
    
    //선호 성별을 검색하는 레파지토리
    @Query("select et from EatTogether et where et.prefgender like %:kw% ")
    Page<EatTogether> findPrefgenderByKeyword(@Param("kw") String kw , Pageable pageable);
    
}
