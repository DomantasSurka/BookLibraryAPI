package com.example.booklibrary;
import java.time.LocalDate;

/***
 * Class about the book.
 */
public class Book {

    /***
     * Book's name.
     */
    private String name;

    /***
     * Book's author.
     */
    private String author;

    /***
     * Book's category.
     */
    private String category;

    /***
     * Book's language.
     */
    private String language;

    /***
     * Book's publication date.
     */
    private LocalDate publicationDate;

    /***
     * Book's ISBN code.
     */
    private Integer ISBN;

    /***
     * Book's unique GUID code.
     */
    private String GUID;

    /***
     * Empty constructor.
     */
    public Book(){}

    /***
     * Full constructor.
     * @param name name of the book.
     * @param author author of the book.
     * @param category category of the book.
     * @param language language of the book.
     * @param publicationDate publication date of the book.
     * @param ISBN ISBN code of the book.
     * @param GUID GUID code of the book.
     */
    public Book(String name, String author, String category, String language, LocalDate publicationDate, Integer ISBN, String GUID) {
        this.name = name;
        this.author = author;
        this.category = category;
        this.language = language;
        this.publicationDate = publicationDate;
        this.ISBN = ISBN;
        this.GUID = GUID;
    }

    /***
     * Book's value of the name setter.
     * @param name book's name
     */
    public void setName(String name) {
        this.name = name;
    }

    /***
     * Book's value of the author setter.
     * @param author book's author
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /***
     * Book's value of the category setter.
     * @param category book's category
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /***
     * Book's value of the language setter.
     * @param language book's language
     */
    public void setLanguage(String language) {
        this.language = language;
    }

    /***
     * Book's value of the publication date setter.
     * @param publicationDate book's publication date
     */
    public void setPublicationDate(LocalDate publicationDate) {
        this.publicationDate = publicationDate;
    }

    /***
     * Book's value of the ISBN setter.
     * @param ISBN book's ISBN code
     */
    public void setISBN(Integer ISBN) {
        this.ISBN = ISBN;
    }

    /***
     * Book's value of the GUID setter.
     * @param GUID book's GUID code
     */
    public void setGUID(String GUID) {
        this.GUID = GUID;
    }

    /***
     * Book's getter of the value language.
     * @return language of the book.
     */
    public String getLanguage() {
        return language;
    }

    /***
     * Book's getter of the value name.
     * @return name of the book.
     */
    public String getName() {
        return name;
    }

    /***
     * Book's getter of the value author.
     * @return author of the book.
     */
    public String getAuthor() {
        return author;
    }

    /***
     * Book's getter of the value category.
     * @return category of the book.
     */
    public String getCategory() {
        return category;
    }

    /***
     * Book's getter of the value publication date.
     * @return publication date of the book.
     */
    public LocalDate getPublicationDate() {
        return publicationDate;
    }

    /***
     * Book's getter of the value ISBN.
     * @return ISBN of the book.
     */
    public Integer getISBN() {
        return ISBN;
    }

    /***
     * Book's getter of the value GUID.
     * @return GUID of the book.
     */
    public String getGUID() {
        return GUID;
    }
}
