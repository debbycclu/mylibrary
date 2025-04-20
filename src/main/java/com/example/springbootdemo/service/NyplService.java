package com.example.springbootdemo.service;

import com.example.springbootdemo.config.NyplConfig;
import com.example.springbootdemo.model.Book;
import com.example.springbootdemo.model.NyplBook;
import com.example.springbootdemo.repository.BookRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class NyplService {

    private final NyplConfig nyplConfig;
    private final RestTemplate restTemplate;
    private final BookRepository bookRepository;
    private final ObjectMapper objectMapper;

    @Autowired
    public NyplService(NyplConfig nyplConfig, BookRepository bookRepository) {
        this.nyplConfig = nyplConfig;
        this.restTemplate = new RestTemplate();
        this.bookRepository = bookRepository;
        this.objectMapper = new ObjectMapper();
    }

    public Optional<Book> createBook(String title) {
        // First, search for the book in NYPL
        List<NyplBook> nyplBooks = searchBooks(title);
        if (nyplBooks.isEmpty()) {
            return Optional.empty();
        }

        // Take the first result and create a book
        NyplBook nyplBook = nyplBooks.get(0);
        
        // Check if book already exists in our database
        Book existingBook = bookRepository.findByNyplId(nyplBook.getNyplId());
        if (existingBook != null) {
            return Optional.of(existingBook);
        }

        // Create new book
        Book book = new Book();
        book.setTitle(nyplBook.getTitle());
        book.setAuthor(nyplBook.getAuthor());
        book.setPublisher(nyplBook.getPublisher());
        book.setPublishYear(nyplBook.getPublishYear());
        book.setIsbn(nyplBook.getIsbn());
        book.setCallNumber(nyplBook.getCallNumber());
        book.setLocation(nyplBook.getLocation());
        book.setStatus(nyplBook.getStatus());
        book.setNyplId(nyplBook.getNyplId());

        return Optional.of(bookRepository.save(book));
    }

    public List<NyplBook> searchBooks(String query) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.set("Authorization", "Bearer " + nyplConfig.getApiKey());

        HttpEntity<String> entity = new HttpEntity<>(headers);

        String url = UriComponentsBuilder.fromHttpUrl(nyplConfig.getBaseUrl() + "/search")
                .queryParam("q", query)
                .toUriString();

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    String.class
            );

            JsonNode root = objectMapper.readTree(response.getBody());
            JsonNode items = root.path("nyplAPI").path("response").path("result");

            List<NyplBook> books = new ArrayList<>();
            for (JsonNode item : items) {
                NyplBook book = new NyplBook();
                book.setTitle(item.path("title").asText());
                book.setAuthor(item.path("author").asText());
                book.setPublisher(item.path("publisher").asText());
                book.setPublishYear(item.path("publishYear").asText());
                book.setIsbn(item.path("isbn").asText());
                book.setCallNumber(item.path("callNumber").asText());
                book.setLocation(item.path("location").asText());
                book.setStatus(item.path("status").asText());
                book.setNyplId(item.path("id").asText());
                books.add(book);
            }
            return books;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public List<Book> searchLocalBooks(String query) {
        return bookRepository.findByTitleContainingIgnoreCase(query);
    }

    public Optional<Book> getBookById(Long id) {
        return bookRepository.findById(id);
    }
} 