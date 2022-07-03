package com.example.booklibrary.services;
import com.example.booklibrary.models.Book;
import com.example.booklibrary.models.BookReservation;
import org.json.simple.*;
import org.json.simple.parser.*;
import java.io.*;
import java.util.*;

public class BookService {

    private static final JSONParser parser = new JSONParser();

    private static final String reservationsFileLocation = "src\\main\\resources\\reservations.json";

    private static final String libraryFileLocation = "src\\main\\resources\\library.json";

    /***
     * Finds a book by its GUID code from books' library file.
     * Parses data file from the storage and reads the list of books in the library.
     * @param GUID GUID code that is unique.
     * @return book with its unique GUID code.
     */
    public static Book findBookByGUID (String GUID) {
        Book book = null;
        JSONArray libraryArr = readFromFile(libraryFileLocation);

        for (Object obj : libraryArr) {
            JSONObject bookObj = (JSONObject) obj;
            if (Objects.equals(bookObj.get("GUID"), GUID)) {
                book = new Book(bookObj);
                break;
            }
        }

        return book;
    }

    /***
     * Gets a list of books from the library.json file.
     * Parses data file from the storage to build books list.
     * @return list of books
     */
    public static List<Book> getListOfBooks() {
        List<Book> list = new ArrayList<>();
        JSONArray libraryArr = readFromFile(libraryFileLocation);

        for(Object obj : libraryArr){
            JSONObject bookObj = (JSONObject) obj;
            list.add(new Book(bookObj));
        }

        return list;
    }

    /***
     * Gets a list of filterBy option parameters. For example, if filterBy
     * option value is an author, then returns all unique authors who are written in the books list.
     * @param books list of all books.
     * @param filterBy value for filter parameters.
     * @return list of filter's parameters values.
     */
    public static HashSet<String> getListOfParameters(List<Book> books, String filterBy) {
        HashSet<String> parameters = new HashSet<>();

        if(Objects.equals(filterBy, "taken or available books")){
            parameters.add("Taken");
            parameters.add("Available");
        } else {
            for (Book book : books) {
                parameters.add(switch (filterBy){
                    case "author" -> book.getAuthor();
                    case "category" -> book.getCategory();
                    case "language" -> book.getLanguage();
                    case "ISBN" -> book.getISBN().toString();
                    case "name" -> book.getName();
                    default -> "";
                });
            }
        }

        return parameters;
    }

    /***
     * Gets books by filterBy parameter value. For example, if filterBy is an author, then gets
     * all the books, whose authors names are like parameter value.
     * @param books list of books, in which searches for the values.
     * @param filterBy value for filter parameters.
     * @param parameter list of filter's parameters values.
     * @return list of books, that meet the filter values.
     */
    public static List<Book> getBooksByParameters(List<Book> books, String filterBy, String parameter) {
        List<Book> listOfBooks = new ArrayList<>();

        if(Objects.equals(parameter, "Taken")){
            listOfBooks = findTakenOrAvailableBooks(books, "Taken");
        } else if(Objects.equals(parameter, "Available")){
            listOfBooks = findTakenOrAvailableBooks(books, "Available");
        } else{
            for (Book book : books) {
                switch (filterBy) {
                    case "author" -> {
                        if(Objects.equals(book.getAuthor(), parameter))
                            listOfBooks.add(book);
                    }
                    case "category" -> {
                        if (Objects.equals(book.getCategory(), parameter))
                            listOfBooks.add(book);
                    }
                    case "language" -> {
                        if (Objects.equals(book.getLanguage(), parameter))
                            listOfBooks.add(book);
                    }
                    case "ISBN" -> {
                        if (Objects.equals(book.getISBN().toString(), parameter))
                            listOfBooks.add(book);
                    }
                    case "name" -> {
                        if (Objects.equals(book.getName(), parameter))
                            listOfBooks.add(book);
                    }
                }
            }
        }

        return listOfBooks;
    }

    /***
     * Finds taken or available books from the books' library list.
     * Parses data file of reservations from the storage, so after books
     * could be checked if they are reserved by a person or available to be taken.
     * @param books list of library books.
     * @param parameter Taken or Available value.
     * @return list of Taken or Available books.
     */
    private static List<Book> findTakenOrAvailableBooks(List<Book> books, String parameter){
        List<Book> list = new ArrayList<>();
        JSONArray reservationsArr = readFromFile(reservationsFileLocation);

        for (Book book : books) {
            boolean exist = false;

            for (Object obj : reservationsArr) {
                JSONObject bookObj = (JSONObject) obj;
                if(Objects.equals(bookObj.get("GUID"), book.getGUID())) {
                    exist = true;
                    break;
                }
            }

            if(!exist && Objects.equals(parameter, "Available")){
                list.add(book);
            } else if(exist && Objects.equals(parameter, "Taken")){
                list.add(book);
            }
        }

        return list;
    }

    /***
     * Removes books by GUID from the data file. Parses data file from the storage
     * and returns its values. Data, that can be removed:
     * books from the library and person's books' reservations.
     * @param GUID GUID unique code of the book.
     * @param fileName name of the file (library or reservations).
     */
    public static void removeBooksByGUID(String GUID, String fileName){
        if(!Objects.equals(fileName, "library") && !Objects.equals(fileName, "reservations")){
            throw new IllegalArgumentException("Wrong file name in removeBooksByGUID(String GUID, String fileName)");
        }

        String location = "src\\main\\resources\\" + fileName + ".json";
        JSONArray dataArray = readFromFile(location);

        for(int i = 0; i < dataArray.size(); i++){
            JSONObject bookObj = (JSONObject) dataArray.get(i);
            if(Objects.equals(bookObj.get("GUID"), GUID)) {
                dataArray.remove(i);
                i--;
            }
        }

        writeToFile(location, dataArray);
    }

    /***
     * Reserves a book from the library. Checks if book exists in the library,
     * if a person has already taken 3 books or if the book is already taken,
     * and returns the message. Saves reservation data to a reservations.json file.
     * @param bookReservation details about book's reservation.
     * @return message about operations' success.
     */
    public static String takeBook(BookReservation bookReservation){
        Book book = findBookByGUID(bookReservation.getBookGUID());

        if(Objects.equals(book, null))
            return "Book does not exist. Try typing book's GUID again.";

        BookReservation reservation = findReservationByGUID(bookReservation.getBookGUID());

        if(!Objects.equals(reservation, null))
            return "Book is already reserved.";

        Integer count = findCountOfTakenBooks(bookReservation.getPerson());

        if(count >= 3)
            return "You have already taken 3 books.";

        saveDataToFile(bookReservation, "reservations");
        return "Book has been successfully taken.";
    }

    /***
     * Private method, which is needed to find reservation by GUID. Parses data
     * file of reservations from the storage and searches for books' unique GUID.
     * @param GUID book's unique GUID code.
     * @return book reservation.
     */
    private static BookReservation findReservationByGUID(String GUID){
        BookReservation reservation = null;
        JSONArray reservationsArr = readFromFile(reservationsFileLocation);

        for(Object obj : reservationsArr){
            JSONObject reservationObj = (JSONObject) obj;
            if(Objects.equals(reservationObj.get("GUID"), GUID)) {
                reservation = new BookReservation(reservationObj);
                break;
            }
        }

        return reservation;
    }

    /***
     * Private method, which is needed to find out how many books a person
     * has already taken. Parses data file of reservations from the storage.
     * @param person detail about the person.
     * @return count of taken books.
     */
    private static Integer findCountOfTakenBooks(String person){
        Integer count = 0;

        JSONArray reservationsArr = readFromFile(reservationsFileLocation);

        for(Object obj : reservationsArr){
            JSONObject reservationsObj = (JSONObject) obj;
            if(Objects.equals(reservationsObj.get("Person"), person))
                count++;
        }

        return count;
    }

    /***
     * Writes data about books or books reservations to a specific .json file.
     * Book or BookReservation class object can be given as a function parameter.
     * @param object Book or BookReservation object.
     * @param fileName name of the file (library or reservations)
     */
    public static void saveDataToFile(Object object, String fileName) {
        if(!Objects.equals(fileName, "library") && !Objects.equals(fileName, "reservations")){
            throw new IllegalArgumentException("Wrong file name in saveDataToFile(Object object, String fileName)");
        }

        String location = "src\\main\\resources\\" + fileName + ".json";
        JSONArray jsonArray = processDataForStorage(object, fileName, location);

        writeToFile(location, jsonArray);
    }

    /***
     * Private method, which tries to process the data of book or reservation and put it into the json array.
     * Book or BookReservation class object can be given as a function parameter.
     * @param object Book or BookReservation object.
     * @param fileName name of the file (library or reservations)
     * @param location location of the data file
     * @return JSON array of data file, in which given object was added.
     */
    @SuppressWarnings("unchecked")
    private static JSONArray processDataForStorage(Object object, String fileName, String location) {
        JSONArray jsonArray = readFromFile(location);

        if (fileName.equals("library")) {
            JSONObject book = ((Book) object).returnBookAsJSONObject();
            jsonArray.add(book);
        } else if (fileName.equals("reservations")) {
            JSONObject reservation = ((BookReservation) object).returnReservationAsJSONObject();
            jsonArray.add(reservation);
        }

        return jsonArray;
    }

    private static void writeToFile(String location, JSONArray data){
        try {
            FileWriter file = new FileWriter(location);
            file.write(data.toJSONString());
            file.flush();
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static JSONArray readFromFile(String location) {
        JSONArray jsonArray = null;

        try {
            jsonArray = (JSONArray) parser.parse(new FileReader(location));
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }

        return jsonArray;
    }
}
