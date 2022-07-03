package com.example.booklibrary.models;
import org.json.simple.JSONObject;

public class BookReservation extends Book {

    private final String person;

    private final Integer period;

    public BookReservation(String person, Integer period, String bookGUID) {
        this.person = person;
        this.period = period;
        this.setBookGUID(bookGUID);
    }

    public BookReservation(JSONObject reservation){
        this.person = reservation.get("Person").toString();
        this.period = Integer.parseInt(reservation.get("Period").toString());
        this.setBookGUID(reservation.get("GUID").toString());
    }

    @SuppressWarnings("unchecked")
    public JSONObject returnReservationAsJSONObject(){
        JSONObject newReservation = new JSONObject();

        newReservation.put("Person", this.getPerson());
        newReservation.put("Period", this.getPeriod());
        newReservation.put("GUID", this.getBookGUID());

        return newReservation;
    }

    public String getPerson() {
        return this.person;
    }

    public Integer getPeriod() {
        return this.period;
    }

    public String getBookGUID() {
        return getGUID();
    }

    public void setBookGUID(String bookGUID) {
        setGUID(bookGUID);
    }
}
