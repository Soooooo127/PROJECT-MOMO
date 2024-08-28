package com.momo.restaurant;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.momo.DataNotFoundException;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RestService {

	private final RestRepository restRepository;
	
	
	public void create(String name , String category) {
		Restaurant rest = new Restaurant();
		rest.setName(name);
		rest.setCategory(category);
		this.restRepository.save(rest);
	}
	
	public Restaurant getRestaurant(Integer no) {
		Optional<Restaurant> rest = this.restRepository.findById(no);
		
		if(rest.isPresent()) {
			return rest.get();
		}else {
			throw new DataNotFoundException("데이터가 없습니다");
		}
		
	}
	
	public List<Restaurant> getList(String category){
		
		Optional<List<Restaurant>> rest = this.restRepository.findByCategory(category);
	
		List<Restaurant> r = rest.get();
	 
		if(rest.isPresent()) {
			return r;
		}else {
			throw new DataNotFoundException("데이터가 없습니다");
		}	
	} 
	
	public List<Restaurant>getListSearch(String kw){
		Specification<Restaurant> spec = search(kw);
		
		
		return this.restRepository.findAll(spec);
	}
	
	
	
	private Specification<Restaurant> search(String kw) {
        return new Specification<>() {
            private static final long serialVersionUID = 1L;
            @Override
            public Predicate toPredicate(Root<Restaurant> r, CriteriaQuery<?> query, CriteriaBuilder cb) {
                query.distinct(true);  // 중복을 제거 
                //Join<Question, SiteUser> u1 = q.join("author", JoinType.LEFT);
                //Join<Question, Answer> a = q.join("answerList", JoinType.LEFT);
                //Join<Answer, SiteUser> u2 = a.join("author", JoinType.LEFT);
                return cb.or(cb.like(r.get("name"), "%" + kw + "%"), // 가게이름 
                        cb.like(r.get("addr"), "%" + kw + "%"),      // 가게주소 
                        cb.like(r.get("menu"), "%" + kw + "%"),		// 가게 메뉴
                       // cb.like(r.get("username"), "%" + kw + "%"),		// 가게 
                        cb.like(r.get("category"), "%" + kw + "%"));      // 음식 카테고리 
                      
            }
        };
    }
}
