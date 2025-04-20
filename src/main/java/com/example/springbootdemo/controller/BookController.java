package com.example.springbootdemo.controller;

import com.example.springbootdemo.model.Book;
import com.example.springbootdemo.service.NyplService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class BookController {

    private final NyplService nyplService;

    @Autowired
    public BookController(NyplService nyplService) {
        this.nyplService = nyplService;
    }

    @GetMapping("/")
    public String home(Model model) {
        // Get all books for the home page
        List<Book> books = nyplService.searchLocalBooks("");
        model.addAttribute("books", books);
        return "home";
    }

    @GetMapping("/search")
    public String search(@RequestParam(required = false) String query, Model model) {
        List<Book> books;
        if (query != null && !query.isEmpty()) {
            books = nyplService.searchLocalBooks(query);
        } else {
            books = nyplService.searchLocalBooks("");
        }
        model.addAttribute("books", books);
        model.addAttribute("query", query);
        return "home";
    }
} 