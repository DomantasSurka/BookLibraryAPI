package com.example.booklibrary;

/***
 * class for book reservation.
 */
public class BookReservation {

    /***
     * Reservation's details about the person.
     */
    private String person;

    /***
     * Reservation's details about the period to take the book.
     */
    private Integer period;

    /***
     * Reservation's details about the book's unique GUID code.
     */
    private String bookGUID;

    /***
     * Empty constructor.
     */
    public BookReservation(){}

    /***
     * Full constructor.
     * @param person details about the person.
     * @param period period of the reservation.
     * @param bookGUID details about the book's GUID.
     */
    public BookReservation(String person, Integer period, String bookGUID) {
        this.person = person;
        this.period = period;
        this.bookGUID = bookGUID;
    }

    /***
     * person value getter.
     * @return person details
     */
    public String getPerson() {
        return person;
    }

    /***
     * period value getter.
     * @return period details
     */
    public Integer getPeriod() {
        return period;
    }

    /***
     * GUID value getter.
     * @return GUID details
     */
    public String getBookGUID() {
        return bookGUID;
    }

    /***
     * person value setter.
     * @param person person details
     */
    public void setPerson(String person) {
        this.person = person;
    }

    /***
     * period value setter.
     * @param period period's details
     */
    public void setPeriod(Integer period) {
        this.period = period;
    }

    /***
     * GUID value setter.
     * @param bookGUID GUID's details
     */
    public void setBookGUID(String bookGUID) {
        this.bookGUID = bookGUID;
    }
}
