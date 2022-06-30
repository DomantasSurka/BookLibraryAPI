package com.example.booklibrary.models;

public class BookReservation extends Book {

    private String person;

    private Integer period;

    public BookReservation(){}

    public BookReservation(String person, Integer period, String bookGUID) {
        this.person = person;
        this.period = period;
        this.setBookGUID(bookGUID);
    }

    public String getPerson() {
        return person;
    }

    public Integer getPeriod() {
        return period;
    }

    public String getBookGUID() {
        return getGUID();
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public void setPeriod(Integer period) {
        this.period = period;
    }

    public void setBookGUID(String bookGUID) {
        this.setGUID(bookGUID);
    }
}
