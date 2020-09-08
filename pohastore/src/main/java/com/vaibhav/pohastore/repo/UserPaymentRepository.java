package com.vaibhav.pohastore.repo;

import org.springframework.data.repository.CrudRepository;

import com.vaibhav.pohastore.domain.UserPayment;

public interface UserPaymentRepository  extends CrudRepository<UserPayment, Long>{

}
