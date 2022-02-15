package com.example.booklibrary;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/***
 * Controller class for Rest API.
 */
@Controller
public class EventController {

    /***
     * Saving value of filterBy selection input, so later can be used for other selection input.
     */
    private String filterBy = "";

    /***
     * Saving value of listed books in /listbooks page, in order to maintain program's
     * efficiency by not calculating it every time, after using other forms on the page.
     */
    private List<Book> booksOfListPage = new ArrayList<>();

    /* ----------------------------------------- */
    /* Add book to the library in /addbook page. */
    /* ----------------------------------------- */

    /***
     * Retrieving resources from /addbook page.
     * @param model Model used for attributes and interaction with the form.
     * @return the same page.
     */
    @GetMapping("/addbook")
    public String addBookPage(Model model) {
        model.addAttribute("book", new String[7]);
        model.addAttribute("message", "");
        return "addbook";
    }

    /***
     * Rest API endpoint to add a new book. All the data is being stored in a JSON file.
     * Location: src\main\resources\library.json
     * Request attribute is set to be String[], to take all inputs as Strings and
     * to prevent type mismatch, so exceptions could be checked and handled.
     * Also writes a message about the operation success.
     * @param bookDetails Request parameter of book details as String array.
     * String[0] - name, String[1] - author, String[2] - category, String[3] - language,
     * String[4] - publication date, String[5] - ISBN, String[6] - GUID.
     * @param model Model used for attributes and interaction with the form.
     */
    @PostMapping("/addbook")
    public void addBookForm(@RequestParam("book") String[] bookDetails, Model model) {
        if(!bookDetails[0].equals("") && !bookDetails[2].equals("") && !bookDetails[3].equals("") && !bookDetails[4].equals("") && !bookDetails[5].equals("") && !bookDetails[6].equals("")){
            try{
                if(bookDetails[4].equals(LocalDate.parse(bookDetails[4]).toString()) && bookDetails[5].equals(String.valueOf(Integer.parseInt(bookDetails[5])))) {
                    Book book = new Book(bookDetails[0], bookDetails[1], bookDetails[2], bookDetails[3], LocalDate.parse(bookDetails[4]), Integer.parseInt(bookDetails[5]), bookDetails[6]);
                    Book bookForCheck = Tasks.findBookByGUID(bookDetails[6]);
                    if(Objects.equals(bookForCheck.getGUID(), null)) {
                        // book, with unique GUID, does not exist.
                        Tasks.saveDataToFile(book, "library");
                        model.addAttribute("message", "Successfully added.");
                    } else{
                        model.addAttribute("message", "Book with specified unique GUID is in the library. Try different GUID.");
                    }
                }
            } catch(Exception e) {
                model.addAttribute("message", "Try again, errors found.");
            }
        } else{
            model.addAttribute("message", "Please fill in all the fields.");
        }
    }

    /* --------------------------------------------- */
    /* Take book from the library in /takebook page. */
    /* --------------------------------------------- */

    /***
     * Retrieving resources from /takebook page.
     * @param model Model used for attributes and interaction with the form.
     * @return the same page.
     */
    @GetMapping("/takebook")
    public String takeBookPage(Model model) {
        model.addAttribute("reservation", new String[3]);
        model.addAttribute("message", "");
        return "takebook";
    }

    /***
     * Rest API endpoint to take a book by its unique GUID. (Every book has unique code - GUID)
     * Book cannot be taken for more than 2 months and the person cannot have more than 3 books taken.
     * Taken books are saved to .json file. Location: src\main\resources\reservations.json
     * Request attribute is set to be String[], to take all inputs as Strings and
     * to prevent type mismatch, so exceptions could be checked and handled.
     * Also writes a message about the operation success.
     * @param reservation Request parameter of reservation details as String array.
     * String[0] - person, String[1] - period, String[2] - GUID
     * @param model Model used for attributes and interaction with the form.
     * @return call to the same page.
     */
    @PostMapping("/takebook")
    public String takeBookPageSubmit(@RequestParam("reservation") String[] reservation, Model model) {
        if(!reservation[0].equals("") && !reservation[1].equals("") && !reservation[2].equals("")) {
            try {
                if (String.valueOf(Integer.parseInt(reservation[1])).equals(reservation[1]) && Integer.parseInt(reservation[1]) <= 60 && Integer.parseInt(reservation[1]) > 0) {
                    BookReservation bookReservation = new BookReservation(reservation[0], Integer.parseInt(reservation[1]), reservation[2]);
                    model.addAttribute("reservation", bookReservation);
                    String message = Tasks.takeBook(bookReservation);
                    model.addAttribute("message", message);
                } else {
                    model.addAttribute("message", "Book can be taken for 1 - 60 days period. Try again.");
                }
            } catch (Exception e) {
                model.addAttribute("message", "Try again, errors found.");
            }
        }
        else{
            model.addAttribute("message", "Please fill in all the fields.");
        }
        return "takebook";
    }

    /* ------------------------------------ */
    /* Get a book by GUID in /getbook page. */
    /* ------------------------------------ */

    /***
     * Retrieving resources from /getbook page.
     * @param model Model used for attributes and interaction with the form.
     * @return the same page.
     */
    @GetMapping("/getbook")
    public String getBookByGUID(Model model) {
        model.addAttribute("GUID", "");
        model.addAttribute("message", "");
        return "getbook";
    }

    /***
     * Rest API endpoint to get a book from books' list from .json file by GUID parameter.
     * Also writes a message about the operation success.
     * @param bookGUID Request parameter book's GUID (identification code) as String.
     * @param model Model used for attributes and interaction with the form.
     */
    @PostMapping("/getbook")
    public void getBookByGUIDSubmit(@RequestParam String bookGUID, Model model) {
        if(!Objects.equals(bookGUID, "")){
            model.addAttribute("GUID", bookGUID);
            Book book = Tasks.findBookByGUID(bookGUID);
            if(!Objects.equals(book.getGUID(), null)) {
                String output = "Book by GUID: " + book.getName();
                model.addAttribute("message", output);
            } else{
                String message = "No book found (GUID: " + bookGUID + ")";
                model.addAttribute("message", message);
            }
        } else{
            model.addAttribute("message", "Please fill in the field.");
        }
    }

    /* --------------------------------------------- */
    /* Books listing and removal in /listbooks page. */
    /* --------------------------------------------- */

    /***
     * Retrieving resources from /listbooks page and displays the list of books.
     * @param model Model used for attributes and interaction with the form.
     * @return the same page.
     */
    @GetMapping("/listbooks")
    public String listBooksPage(Model model) {
        this.booksOfListPage = Tasks.getListOfBooks();
        model.addAttribute("books", booksOfListPage);
        model.addAttribute("parameters", "");
        model.addAttribute("filter", "");
        model.addAttribute("displayFirstFilter", "");
        model.addAttribute("showFilterMessage", "");
        model.addAttribute("showFilter", "");
        model.addAttribute("displaySecondFilter", "");
        if(booksOfListPage.size() == 0){
            model.addAttribute("showFilter", "showFilter");
        }
        String message = "Showing " + booksOfListPage.size() + " books";
        model.addAttribute("message", message);
        return "listbooks";
    }

    /***
     * Rest API endpoint for page /listbooks, to select a "filter by" value, so later in the next select input, user
     * could be able to select a parameter of the "filter by" input.
     * @param selected Request parameter of selected value from selection input.
     * @param model Model used for attributes and interaction with the form.
     */
    @PostMapping(value="/listbooks", params="submit1")
    public void filterBy(@RequestParam String selected, Model model) {
        model.addAttribute("filter", selected);
        List<String> foundParameters = Tasks.getListOfParameters(booksOfListPage, selected);
        model.addAttribute("parameters", foundParameters);
        model.addAttribute("books", booksOfListPage);
        if(booksOfListPage.size() > 0) {
            model.addAttribute("displaySecondFilter", "show");
            this.filterBy = selected;
            model.addAttribute("displayFirstFilter", 0);
        }
        String message = "Showing " + booksOfListPage.size() + " books";
        model.addAttribute("message", message);
    }

    /***
     * Rest API endpoint for page /listbooks, to select a parameter of "filter by" input and filter books list.
     * Writes a message how the list has been filtered.
     * @param selectedFilter Request parameter of selected value from selection input.
     * @param model Model used for attributes and interaction with the form.
     */
    @PostMapping(value="/listbooks", params="submit2")
    public void chooseByFilter(@RequestParam String selectedFilter, Model model) {
        List<Book> books = this.booksOfListPage;
        List<Book> filteredBooks = Tasks.getBooksByParameters(books, filterBy, selectedFilter);
        String filterMessage = "Books list filtered: " + filterBy + " → " + selectedFilter;
        model.addAttribute("filterMessage", filterMessage);
        model.addAttribute("books", filteredBooks);
        model.addAttribute("showFilterMessage", "showFilterMessage");
        model.addAttribute("showFilter", "showFilter");
        String message = "Showing " + filteredBooks.size() + " books";
        model.addAttribute("message", message);
    }

    /***
     * Rest API endpoint for page /listbooks, to delete a book from the list.
     * Book is being identified by GUID code and deleted from the library
     * and reservations file. When page reloads, it also reloads the list of
     * books displayed on the page.
     * @param param Request parameter of selected button.
     * @param model Model used for attributes and interaction with the form.
     * @return refreshed same page.
     */
    @PostMapping(value="/listbooks", params="submit3")
    public String deleteBook(@RequestParam("submit3") String param, Model model) {
        model.addAttribute("GUID", param);
        Tasks.removeBooksByGUID(param, "library");
        Tasks.removeBooksByGUID(param, "reservations");
        // after removal page needs to be refreshed
        return "redirect:/listbooks";
    }
}
