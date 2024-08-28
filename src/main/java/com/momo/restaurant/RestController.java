package com.momo.restaurant;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.momo.restaurant.et.EatTogether;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/rest")
public class RestController {

	private final RestService restService;
	
	@GetMapping("/")
	public String restAll() {
		return "rest/rest_all";
	}
	
	//@ResponseBody JSON 으로 보내
	@GetMapping("/detail/{no}")
	public String restDetail(Model model, @PathVariable("no")Integer no) {
		Restaurant rest = this.restService.getRestaurant(no);
		
		List<EatTogether> etList = rest.getEtList();
		
		LocalDate today = LocalDate.now();
		Integer expired = 0;
		for(EatTogether et : etList) {
			if(today.isAfter(et.getEtdate().toLocalDate())) {
				expired += 1;
				
			}
		}
		rest.setProgresset(etList.size() - expired);
		
		model.addAttribute("rest", rest);
		
		return "rest/rest_detail";
	}
	
	
	@GetMapping("/list/{category}")
	public String restList(Model model, @PathVariable("category") String category) {
	
		
		List<Restaurant>restList = this.restService.getList(category);
		model.addAttribute("restList", restList);
		return "rest/rest_list";
	}
	
	@GetMapping("/list/search")
	public String restListSerch(Model model, @RequestParam(value = "kw", defaultValue = "") String kw) {
		List<Restaurant>restList = this.restService.getListSearch(kw);
		model.addAttribute("restList", restList);
		model.addAttribute("kw", kw);
		

		
		return "rest/rest_search";
	}
	
	@GetMapping("/eat")
	public String eat() {
		
		return "rest/eat";
	}

	
	
}
