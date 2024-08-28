package com.momo.restaurant.et;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EatTogetherForm {

	@NotEmpty(message = "제목을 작성해주세요")
	@Size(max = 100)
	private String ettitle;
	
	@NotNull(message = "날짜를 선택해주세요")
	@Future
	private LocalDateTime etdate;
	
	@NotEmpty(message = "인원수를 선택해주세요")
	private String prtnumber;
	
	private String prefgender;
	
	private String prefmbti;
}
