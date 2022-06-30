package com.example.booklibrary.models;
import java.time.LocalDate;

public class Book {

    private String name;

    private String author;

    private String category;

    private String language;

    private LocalDate publicationDate;

    private Integer ISBN;

    private String GUID;

    public Book(){}

    public Book(String name, String author, String category, String language, LocalDate publicationDate, Integer ISBN, String GUID) {
        this.name = name;
        this.author = author;
        this.category = category;
        this.language = language;
        this.publicationDate = publicationDate;
        this.ISBN = ISBN;
        this.GUID = GUID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setPublicationDate(LocalDate publicationDate) {
        this.publicationDate = publicationDate;
    }

    public void setISBN(Integer ISBN) {
        this.ISBN = ISBN;
    }

    public void setGUID(String GUID) {
        this.GUID = GUID;
    }

    public String getLanguage() {
        return language;
    }

    public String getName() {
        return name;
    }

    public String getAuthor() {
        return author;
    }

    public String getCategory() {
        return category;
    }

    public LocalDate getPublicationDate() {
        return publicationDate;
    }

    public Integer getISBN() {
        return ISBN;
    }

    public String getGUID() {
        return GUID;
    }
}
