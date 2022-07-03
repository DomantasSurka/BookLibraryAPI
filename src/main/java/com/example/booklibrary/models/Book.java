package com.example.booklibrary.models;
import org.json.simple.JSONObject;
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

    public Book(JSONObject book){
        this.name = book.get("Name").toString();
        this.author = book.get("Author").toString();
        this.category = book.get("Category").toString();
        this.language = book.get("Language").toString();
        this.publicationDate = LocalDate.parse(book.get("Publication date").toString());
        this.ISBN = Integer.parseInt(book.get("ISBN").toString());
        this.GUID = book.get("GUID").toString();
    }

    @SuppressWarnings("unchecked")
    public JSONObject returnBookAsJSONObject(){
        JSONObject newBook = new JSONObject();

        newBook.put("Name", this.getName());
        newBook.put("Author", this.getAuthor());
        newBook.put("Category", this.getCategory());
        newBook.put("Language", this.getLanguage());
        newBook.put("Publication date", this.getPublicationDate().toString());
        newBook.put("ISBN", this.getISBN());
        newBook.put("GUID", this.getGUID());

        return newBook;
    }

    public void setGUID(String GUID) {
        this.GUID = GUID;
    }

    public String getLanguage() {
        return this.language;
    }

    public String getName() {
        return this.name;
    }

    public String getAuthor() {
        return this.author;
    }

    public String getCategory() {
        return this.category;
    }

    public LocalDate getPublicationDate() {
        return this.publicationDate;
    }

    public Integer getISBN() {
        return this.ISBN;
    }

    public String getGUID() {
        return this.GUID;
    }
}
