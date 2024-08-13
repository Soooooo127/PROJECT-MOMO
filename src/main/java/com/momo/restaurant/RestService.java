package com.momo.restaurant;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RestService {

	private final RestRepository restRepository;
}
