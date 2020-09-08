package com.vaibhav.pohastore.serviceimpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vaibhav.pohastore.domain.Book;
import com.vaibhav.pohastore.repo.BookRepository;
import com.vaibhav.pohastore.service.BookService;


@Service
public class BookServiceImpl implements BookService {
	
	
	@Autowired
	private BookRepository bookRepository;
	

	@Override
	public List<Book> findAllBook() {
		// TODO Auto-generated method stub
		return (List<Book>) bookRepository.findAll();
	}


	@Override
	public Book findOne(Long id) {
		// TODO Auto-generated method stub
		return bookRepository.findById(id).orElse(null);
	}
	
	

}
