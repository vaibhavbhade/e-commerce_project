package com.vaibhav.pohastore.repo;

import org.springframework.data.repository.CrudRepository;

import com.vaibhav.pohastore.domain.ShoppingCart;

public interface ShoppingCartRepository extends CrudRepository<ShoppingCart, Long>{

}
