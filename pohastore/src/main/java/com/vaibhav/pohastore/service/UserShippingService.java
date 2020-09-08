package com.vaibhav.pohastore.service;

import com.vaibhav.pohastore.domain.UserShipping;

public interface UserShippingService {
	
	UserShipping findById(Long id);

	void removeById(Long userShippingId);

}
