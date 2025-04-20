package com.example.springbootdemo.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String title;
    private String author;
    private String publisher;
    private String publishYear;
    private String isbn;
    private String callNumber;
    private String location;
    private String status;
    
    @Column(unique = true)
    private String nyplId; // To store NYPL's unique identifier for the book
} 