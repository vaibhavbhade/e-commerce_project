package com.vaibhav.pohastore.repo;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;

import com.vaibhav.pohastore.domain.BookToCartItem;
import com.vaibhav.pohastore.domain.CartItem;


@Transactional
public interface BookToCartItemRepository extends CrudRepository<BookToCartItem, Long> {

	void deleteByCartItem(CartItem cartItem);

}
