package com.example.springbootdemo.service;

import com.example.springbootdemo.model.Book;
import com.example.springbootdemo.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {
    
    @Autowired
    private BookRepository bookRepository;
    
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }
    
    public Book getBookById(Long id) {
        return bookRepository.findById(id).orElse(null);
    }
    
    public Book createBook(Book book) {
        return bookRepository.save(book);
    }
    
    public Book updateBook(Long id, Book book) {
        if (bookRepository.existsById(id)) {
            book.setId(id);
            return bookRepository.save(book);
        }
        return null;
    }
    
    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }
} 