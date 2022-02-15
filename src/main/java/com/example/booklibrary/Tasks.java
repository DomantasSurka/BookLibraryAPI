package com.example.booklibrary;
import org.json.simple.*;
import org.json.simple.parser.*;
import java.io.*;
import java.time.LocalDate;
import java.util.*;

/***
 * class for calculations and tasks.
 */
public class Tasks {

    /***
     * Writes data about books or books reservations to a .json file.
     * Book or BookReservation class object can be given as a function parameter.
     * @param object Book or BookReservation object.
     * @param fileName name of the file (library or reservations)
     */
    public static void saveDataToFile(Object object, String fileName) {
        if(!Objects.equals(fileName, "library") && !Objects.equals(fileName, "reservations")){
            throw new IllegalArgumentException("Wrong file name in saveDataToFile(Object object, String fileName)");
        }
        JSONParser jsonParser = new JSONParser();
        JSONArray jsonArray = new JSONArray();
        String location = "src\\main\\resources\\" + fileName + ".json";
        try {
            Object obj = jsonParser.parse(new FileReader(location));
            jsonArray = (JSONArray) obj;
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }
        try {
            if(object instanceof Book book) {
                JSONObject newBook = new JSONObject();
                newBook.put("Name", book.getName());
                newBook.put("Author", book.getAuthor());
                newBook.put("Category", book.getCategory());
                newBook.put("Language", book.getLanguage());
                newBook.put("Publication date", book.getPublicationDate().toString());
                newBook.put("ISBN", book.getISBN());
                newBook.put("GUID", book.getGUID());
                jsonArray.add(newBook);
            } else if(object instanceof BookReservation reservation) {
                JSONObject newBook = new JSONObject();
                newBook.put("Person", reservation.getPerson());
                newBook.put("Period", reservation.getPeriod());
                newBook.put("GUID", reservation.getBookGUID());
                jsonArray.add(newBook);
            }
            FileWriter file = new FileWriter(location);
            file.write(jsonArray.toJSONString());
            file.flush();
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /***
     * Finds a book by its GUID code from books' library file.
     * Parses data file from the storage and reads the list of books in the library.
     * @param GUID GUID code that is unique.
     * @return book with its unique GUID code.
     */
    public static Book findBookByGUID(String GUID){
        Book book = new Book();
        JSONParser parser = new JSONParser();
        String location = "src\\main\\resources\\library.json";
        try {
            JSONArray tmpArr = (JSONArray)parser.parse(new FileReader(location));
            for(Object obj : tmpArr){
                JSONObject tmpObj = (JSONObject) obj;
                if(Objects.equals(tmpObj.get("GUID").toString(), GUID)) {
                    book.setName(tmpObj.get("Name").toString());
                    book.setAuthor(tmpObj.get("Author").toString());
                    book.setCategory(tmpObj.get("Category").toString());
                    book.setLanguage(tmpObj.get("Language").toString());
                    book.setPublicationDate(LocalDate.parse(tmpObj.get("Publication date").toString()));
                    book.setISBN(Integer.parseInt(tmpObj.get("ISBN").toString()));
                    book.setGUID(tmpObj.get("GUID").toString());
                    break;
                }
            }
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }
        return book;
    }

    /***
     * Gets a list of books from the library.json file.
     * Parses data file from the storage to build books list.
     * @return list of books
     */
    public static List<Book> getListOfBooks() {
        JSONParser parser = new JSONParser();
        List<Book> list = new ArrayList<>();
        try {
            JSONArray tmpArr = (JSONArray)parser.parse(new FileReader("src\\main\\resources\\library.json"));
            for(Object obj : tmpArr){
                JSONObject tmpObj = (JSONObject) obj;
                    list.add(new Book(tmpObj.get("Name").toString(), tmpObj.get("Author").toString(), tmpObj.get("Category").toString(), tmpObj.get("Language").toString(),
                            LocalDate.parse(tmpObj.get("Publication date").toString()), Integer.parseInt(tmpObj.get("ISBN").toString()), tmpObj.get("GUID").toString()));
            }
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    /***
     * Gets a list of filterBy option parameters. For example, if filterBy
     * option value is an author, then returns all authors who are written in the books list.
     * @param books list of all books.
     * @param filterBy value for filter parameters.
     * @return list of filter's parameters values.
     */
    public static List<String> getListOfParameters(List<Book> books, String filterBy) {
        List<String> parameters = new ArrayList<>();
        if(Objects.equals(filterBy, "taken or available books")){
            parameters.add("Taken");
            parameters.add("Available");
        } else {
            for (Book book : books) {
                switch (filterBy) {
                    case "author":
                        if (!parameters.contains(book.getAuthor()))
                            parameters.add(book.getAuthor());
                        break;
                    case "category":
                        if (!parameters.contains(book.getCategory()))
                            parameters.add(book.getCategory());
                        break;
                    case "language":
                        if (!parameters.contains(book.getLanguage()))
                            parameters.add(book.getLanguage());
                        break;
                    case "ISBN":
                        if (!parameters.contains(book.getISBN().toString()))
                            parameters.add(book.getISBN().toString());
                        break;
                    case "name":
                        if (!parameters.contains(book.getName()))
                            parameters.add(book.getName());
                        break;
                }
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
                    case "author":
                        if (Objects.equals(book.getAuthor(), parameter))
                            listOfBooks.add(book);
                        break;
                    case "category":
                        if (Objects.equals(book.getCategory(), parameter))
                            listOfBooks.add(book);
                        break;
                    case "language":
                        if (Objects.equals(book.getLanguage(), parameter))
                            listOfBooks.add(book);
                        break;
                    case "ISBN":
                        if (Objects.equals(book.getISBN().toString(), parameter))
                            listOfBooks.add(book);
                        break;
                    case "name":
                        if (Objects.equals(book.getName(), parameter))
                            listOfBooks.add(book);
                        break;
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
        JSONParser parser = new JSONParser();
        List<Book> list = new ArrayList<>();
        String location = "src\\main\\resources\\reservations.json";
        try {
            JSONArray tmpArr = (JSONArray)parser.parse(new FileReader(location));
            for (Book book : books) {
                boolean exist = false;
                for (Object obj : tmpArr) {
                    JSONObject tmpObj = (JSONObject) obj;
                    if(Objects.equals(tmpObj.get("GUID").toString(), book.getGUID())) {
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
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    /***
     * Removes books by GUID from the data file. Parses data file from the storage
     * and returns its values. Data, that can be removed: books
     * from the library and person's books' reservations.
     * @param GUID GUID unique code of the book.
     * @param fileName name of the file (library or reservations).
     */
    public static void removeBooksByGUID(String GUID, String fileName){
        if(!Objects.equals(fileName, "library") && !Objects.equals(fileName, "reservations")){
            throw new IllegalArgumentException("Wrong file name in removeBooksByGUID(String GUID, String fileName)");
        }
        JSONParser parser = new JSONParser();
        JSONArray jsonArray = new JSONArray();
        String location = "src\\main\\resources\\" + fileName + ".json";
        try {
            jsonArray = (JSONArray)parser.parse(new FileReader(location));
            for(int i = 0; i < jsonArray.size(); i++){
                JSONObject tmpObj = (JSONObject) jsonArray.get(i);
                if(Objects.equals(tmpObj.get("GUID").toString(), GUID)) {
                    jsonArray.remove(i);
                    i--;
                }
            }
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }
        try {
            FileWriter file = new FileWriter(location);
            file.write(jsonArray.toJSONString());
            file.flush();
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /***
     * Reserves a book from the library. Checks if book exists in the library,
     * if a person has already taken 3 books or if the book is already taken,
     * and returns the message. Saves reservation data to a reservations.json file.
     * @param bookReservation details about book's reservation.
     * @return message about operations' success.
     */
    public static String takeBook(BookReservation bookReservation){
        String message;
        Book book = findBookByGUID(bookReservation.getBookGUID());
        if(!Objects.equals(book.getGUID(), null)){
            // book exists
            BookReservation reservation = findReservationByGUID(bookReservation.getBookGUID());
            if(Objects.equals(reservation.getBookGUID(), null)){
                // book can be reserved
                Integer count = findCountOfTakenBooks(bookReservation.getPerson());
                if(count < 3){
                    saveDataToFile(bookReservation, "reservations");
                    message = "Book has been successfully taken.";
                } else{
                    // max 3 books allowed to be taken.
                    message = "You have already taken 3 books.";
                }
            } else{
                // book is already taken
                message = "Book is already reserved.";
            }
        } else{
            // book does not exist
            message = "Book does not exist. Try typing book's GUID again.";
        }
        return message;
    }

    /***
     * Private method, which is needed to find reservation by GUID. Parses data
     * file of reservations from the storage and searches for books' unique GUID.
     * @param GUID book's unique GUID code.
     * @return book reservation.
     */
    private static BookReservation findReservationByGUID(String GUID){
        BookReservation reservation = new BookReservation();
        JSONParser parser = new JSONParser();
        String location = "src\\main\\resources\\reservations.json";
        try {
            JSONArray tmpArr = (JSONArray)parser.parse(new FileReader(location));
            for(Object obj : tmpArr){
                JSONObject tmpObj = (JSONObject) obj;
                if(Objects.equals(tmpObj.get("GUID").toString(), GUID)) {
                    reservation.setPerson(tmpObj.get("Person").toString());
                    reservation.setPeriod(Integer.parseInt(tmpObj.get("Period").toString()));
                    reservation.setBookGUID(tmpObj.get("GUID").toString());
                    break;
                }
            }
        } catch (ParseException | IOException e) {
            e.printStackTrace();
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
        JSONParser parser = new JSONParser();
        String location = "src\\main\\resources\\reservations.json";
        try {
            JSONArray tmpArr = (JSONArray)parser.parse(new FileReader(location));
            for(Object obj : tmpArr){
                JSONObject tmpObj = (JSONObject) obj;
                if(Objects.equals(tmpObj.get("Person").toString(), person)) {
                    count++;
                }
            }
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }
        return count;
    }
}
