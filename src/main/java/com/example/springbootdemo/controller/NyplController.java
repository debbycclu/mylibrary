package com.example.springbootdemo.controller;

import com.example.springbootdemo.model.Book;
import com.example.springbootdemo.model.NyplBook;
import com.example.springbootdemo.service.NyplService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/nypl")
public class NyplController {

    private final NyplService nyplService;

    @Autowired
    public NyplController(NyplService nyplService) {
        this.nyplService = nyplService;
    }

    @PostMapping("/books")
    public ResponseEntity<?> createBook(@RequestParam String title) {
        Optional<Book> book = nyplService.createBook(title);
        if (book.isPresent()) {
            return ResponseEntity.ok(book.get());
        } else {
            return ResponseEntity.badRequest().body("Book not found in NYPL database");
        }
    }

    @GetMapping("/books/search")
    public ResponseEntity<List<Book>> searchLocalBooks(@RequestParam String query) {
        List<Book> books = nyplService.searchLocalBooks(query);
        return ResponseEntity.ok(books);
    }

    @GetMapping("/books/{id}")
    public ResponseEntity<?> getBookById(@PathVariable Long id) {
        Optional<Book> book = nyplService.getBookById(id);
        if (book.isPresent()) {
            return ResponseEntity.ok(book.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<NyplBook>> searchNyplBooks(@RequestParam String query) {
        List<NyplBook> books = nyplService.searchBooks(query);
        return ResponseEntity.ok(books);
    }
} 