package com.vaibhav.pohastore.repo;

import org.springframework.data.repository.CrudRepository;

import com.vaibhav.pohastore.domain.Book;

public interface BookRepository  extends CrudRepository<Book, Long>{
	
	 

}
