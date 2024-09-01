package com.momo.restaurant.et;



import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.momo.DataNotFoundException;
import com.momo.member.Member;
import com.momo.restaurant.Restaurant;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EatTogetherService {

	private final EatTogetherRepository etRepository;
	
	//같이먹기 등록 서비스구문
	public void create(Restaurant rest , Member applymember , String ettitle , LocalDateTime etdate 
			, String prtnumber , String prefgender , String prefmbti) {
		EatTogether et = new EatTogether();
		List<Member> list = new ArrayList<>();
		list.add(applymember);
		et.setPrtmember(list);
		et.setRest(rest);
		et.setApplymember(applymember);
		et.setEttitle(ettitle);
		et.setRegdate(LocalDateTime.now());
		et.setEtdate(etdate);
		et.setPrtnumber(prtnumber);
		et.setPrefgender(prefgender);
		et.setPrefmbti(prefmbti);
		this.etRepository.save(et);
		System.out.println("같이먹기 등록 데이터입력완료");
	}
	
	//같이먹기 전체 리스트 등록날짜의 내림차순으로 출력하기
	public List<EatTogether> getListAll(){
		return this.etRepository.findAllOrderByDesc();
	}
	
	//가게 정보로 같이먹기 리스트 등록날짜의 내림차순으로 출력하기
	public List<EatTogether> getList(Restaurant rest) {
		
		return this.etRepository.findByRestOrderByDesc(rest);
	}
	
	//마이페이지 내 같이 먹기 + 페이징 + 검색
		public Page<EatTogether> getMyET(Member momoMember, String ettitle, int page) {
			List<Sort.Order> sorts = new ArrayList<>();
			sorts.add(Sort.Order.asc("etdate"));
			Pageable pageable = PageRequest.of(page,  3, Sort.by(sorts));
			return this.etRepository.findByAuthorAndEttitle(momoMember, ettitle, pageable);
		}
	
	//같이먹기 참여 서비스구문
	public void participate(EatTogether et , Member momoMember) {
		
		et.getPrtmember().add(momoMember);
		this.etRepository.save(et);
	}
	
	//같이먹기 조회
	public EatTogether getET(Integer no) {
		Optional<EatTogether> et = this.etRepository.findById(no);
		if(et.isPresent()) {
			return et.get();
		}else {
			throw new DataNotFoundException("No Data");
		}
	}
	
	//같이먹기 삭제
	public void delete(EatTogether et) {
		this.etRepository.delete(et);
	}
	
	//같이먹기 나가기
	public void leave(EatTogether et , Member momoMember) {
		et.getPrtmember().remove(momoMember);
		this.etRepository.save(et);
	}
	/*
	//같이먹기 날짜비교
	public Integer compareDate(EatTogether et) {
		LocalDate date1 = LocalDate.now();
		LocalDateTime etdate = et.getEtdate();
		if(date1.isAfter(etdate.toLocalDate())) {
			return 1;
		}else {
			return 0;
		}
		
	}*/
	/*
	//같이먹기 자동삭제
	@Transactional
	@Async
	@Scheduled(cron = "0 0 0 * * *")
	public void autoDelete(EatTogether et) {
		etRepository.deleteByCreatedAtLessThanEqual(et.getEtDate().minusDays(0));
	}
	*/
}
