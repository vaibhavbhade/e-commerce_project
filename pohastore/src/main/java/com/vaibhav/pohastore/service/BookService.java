package com.vaibhav.pohastore.service;

import java.util.List;
import java.util.Optional;

import com.vaibhav.pohastore.domain.Book;

public interface BookService {

public List<Book> findAllBook();
public Book findOne(Long id);
	
}
