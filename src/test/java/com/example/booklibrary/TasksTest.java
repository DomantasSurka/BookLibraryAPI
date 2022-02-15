package com.example.booklibrary;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/***
 * Tests class for testing Tasks class methods.
 */
class TasksTest {

    /* ------------------------------------------------------------------------------------------ */
    /* Before starting tests, please make sure data files are empty (only containing symbol "[]") */
    /* ------------------------------------------------------------------------------------------ */

    /***
     * Testing method saveDataToFile(),
     * when the given fileName is not library or reservations - method throws an exception.
     */
    @Test
    void saveDataToFile_wrongGivenFileName_shouldThrow() {
        // Arrange
            String fileName = "bookLibrary";
            Book book = new Book("Name", "Author", "Category", "Language", LocalDate.parse("2000-10-10"), 100, "1064A");
        // Act
            IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> Tasks.saveDataToFile(book, fileName));
        // Assert
            assertEquals("Wrong file name in saveDataToFile(Object object, String fileName)", thrown.getMessage());
    }

    /***
     * Testing method findBookByGUID(),
     * when there is no book by GUID - method returns empty book object.
     */
    @Test
    void findBookByGUID_noBookFound_returnsEmptyBookObject() {
        // Arrange
            String GUID = "1005T";
        // Act
            Book book = Tasks.findBookByGUID(GUID);
        // Assert
            assertNull(book.getName());
            assertNull(book.getGUID());
    }

    /***
     * Testing method findBookByGUID(),
     * when book exist - method returns book object.
     */
    @Test
    void findBookByGUID_bookExist_returnsBookObject() {
        // Arrange
            String expectedGUID = "1005T";
            Book book = new Book("Name", "Author", "Category", "Language", LocalDate.parse("2020-12-12"), 100, expectedGUID);
            // Now object is added, later it will be removed.
            Tasks.saveDataToFile(book, "library");
        // Act
            Book bookByGUID = Tasks.findBookByGUID(expectedGUID);
            // After action, library can be cleared.
            Tasks.removeBooksByGUID(expectedGUID, "library");
        // Assert
            assertEquals(expectedGUID, bookByGUID.getGUID());
    }

    /***
     * Testing method getListOfBooks(),
     * when data file is empty - method returns empty book list.
     */
    @Test
    void getListOfBooks_dataFileIsEmpty_returnsEmptyBookList() {
        // Arrange
            List<Book> list;
        // Act
            list = Tasks.getListOfBooks();
        // Assert
            assertEquals(0, list.size());
    }

    /***
     * Testing method getListOfBooks(),
     * when data file has 3 values - method returns all the values.
     */
    @Test
    void getListOfBooks_dataFileHasThreeValues_returnsAllTheValues() {
        // Arrange
            List<Book> list;
            // Book1, GUID - 1
            Book book1 = new Book("Book1", "Author", "Category", "Language", LocalDate.parse("2020-12-12"), 100, "1");
            Tasks.saveDataToFile(book1, "library");
            // Book2, GUID - 2
            Book book2 = new Book("Book2", "Author", "Category", "Language", LocalDate.parse("2020-12-12"), 200, "2");
            Tasks.saveDataToFile(book2, "library");
            // Book3, GUID - 3
            Book book3 = new Book("Book3", "Author", "Category", "Language", LocalDate.parse("2020-12-12"), 300, "3");
            Tasks.saveDataToFile(book3, "library");
        // Act
            list = Tasks.getListOfBooks();
            // clearing library
            Tasks.removeBooksByGUID("1", "library");
            Tasks.removeBooksByGUID("2", "library");
            Tasks.removeBooksByGUID("3", "library");
        // Assert
            assertEquals(3, list.size());
            assertEquals(list.get(0).getGUID(), "1");
            assertEquals(list.get(1).getGUID(), "2");
            assertEquals(list.get(2).getGUID(), "3");
    }

    /***
     * Testing method getListOfParameters(),
     * when the list is empty, there will be no parameters to filterBy, so
     * method returns empty parameter list.
     */
    @Test
    void getListOfParameters_noParametersFound_returnsEmptyParametersList() {
        // Arrange
            List<Book> list = new ArrayList<>();
            List<String> parameters;
            String filterBy = "Author";
        // Act
            parameters = Tasks.getListOfParameters(list, filterBy);
        // Assert
            assertEquals(0, parameters.size());
    }

    /***
     * Testing method getListOfParameters(),
     * when the list is not empty, there will be parameters to filterBy, so
     * method returns parameter list.
     */
    @Test
    void getListOfParameters_parametersFound_returnsParametersList() {
        // Arrange
            List<Book> list = new ArrayList<>();
            list.add(new Book("Book1", "Author1", "Category1", "Language1", LocalDate.parse("2020-12-12"), 100, "1"));
            list.add(new Book("Book2", "Author2", "Category2", "Language2", LocalDate.parse("2020-12-12"), 200, "2"));
            list.add(new Book("Book3", "Author3", "Category3", "Language3", LocalDate.parse("2020-12-12"), 300, "3"));
            List<String> parameters;
            String filterBy = "author";
        // Act
            parameters = Tasks.getListOfParameters(list, filterBy);
        // Assert
            assertEquals(3, parameters.size());
            assertEquals("Author1", parameters.get(0));
            assertEquals("Author2", parameters.get(1));
            assertEquals("Author3", parameters.get(2));
    }

    /***
     * Testing method getListOfParameters(),
     * when the list is not empty, there will be parameters to filterBy,
     * and when there are 2 books with the same category (same parameters),
     * so method returns only one parameter in the list.
     */
    @Test
    void getListOfParameters_sameParametersFound_returnsSameParametersList() {
        // Arrange
            List<Book> list = new ArrayList<>();
            list.add(new Book("Book1", "Author1", "Category", "Language", LocalDate.parse("2020-12-12"), 100, "1"));
            list.add(new Book("Book2", "Author2", "Category", "Language", LocalDate.parse("2020-12-12"), 200, "2"));
            List<String> parameters;
            String filterByCategory = "category";
        // Act
            parameters = Tasks.getListOfParameters(list, filterByCategory);
        // Assert
            assertEquals(1, parameters.size());
            assertEquals("Category", parameters.get(0));
    }

    /***
     * Testing method getBooksByParameters(),
     * when books list is empty and only at conditions, when
     * filterBy is "taken or available books", because its parameters are not
     * related to book's details - method returns empty parameter list.
     */
    @Test
    void getBooksByParameters_booksListEmpty_returnsEmptyBookList() {
        // Arrange
            List<Book> list = new ArrayList<>();
            List<Book> listAfterFiltering;
            String filterBy = "taken or available books";
            String parameter = "Taken";
        // Act
            listAfterFiltering = Tasks.getBooksByParameters(list, filterBy, parameter);
        // Assert
            assertEquals(0, listAfterFiltering.size());
    }

    /***
     * Testing method getBooksByParameters(),
     * when books list is not empty and filters by author "Author1", when
     * list has only 1 book of "Author1" - method returns list of books with only 1 book.
     */
    @Test
    void getBooksByParameters_listHas3Books_returnsBookList() {
        // Arrange
            List<Book> list = new ArrayList<>();
            list.add(new Book("Book1", "Author1", "Category", "Language", LocalDate.parse("2020-12-12"), 100, "1"));
            list.add(new Book("Book2", "Author2", "Category", "Language", LocalDate.parse("2020-12-12"), 200, "2"));
            list.add(new Book("Book3", "Author3", "Category", "Language", LocalDate.parse("2020-12-12"), 300, "3"));
            List<Book> listAfterFiltering;
            String filterBy = "author";
            String parameter = "Author1";
        // Act
            listAfterFiltering = Tasks.getBooksByParameters(list, filterBy, parameter);
        // Assert
            assertEquals(1, listAfterFiltering.size());
    }

    /***
     * Testing method getBooksByParameters(),
     * when books list is not empty and filters by author "Author1", when
     * list has 3 books of "Author1" - method returns list of books with all author's books.
     */
    @Test
    void getBooksByParameters_listHas3BooksOfSameAuthor_returnsThreeBooks() {
        // Arrange
            List<Book> list = new ArrayList<>();
            list.add(new Book("Book1", "Author1", "Category", "Language", LocalDate.parse("2020-12-12"), 100, "1"));
            list.add(new Book("Book2", "Author1", "Category", "Language", LocalDate.parse("2020-12-12"), 200, "2"));
            list.add(new Book("Book3", "Author1", "Category", "Language", LocalDate.parse("2020-12-12"), 300, "3"));
            List<Book> listAfterFiltering;
            String filterBy = "author";
            String parameter = "Author1";
        // Act
            listAfterFiltering = Tasks.getBooksByParameters(list, filterBy, parameter);
        // Assert
            assertEquals(3, listAfterFiltering.size());
    }

    /***
     * Testing method removeBooksByGUID(),
     * when the given fileName is not library or reservations - method throws an exception.
     */
    @Test
    void removeBooksByGUID_wrongGivenFileName_shouldThrow() {
        // Arrange
            String fileName = "bookLibrary";
            String GUID = "1064A";
        // Act
            IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> Tasks.removeBooksByGUID(GUID, fileName));
        // Assert
            assertEquals("Wrong file name in removeBooksByGUID(String GUID, String fileName)", thrown.getMessage());
    }

    /***
     * Testing method removeBooksByGUID(),
     * when library of books has a book and removal with not existing GUID is done,
     * library of books remains the same, none error happens.
     */
    @Test
    void removeBooksByGUID_givenBookGUIDDoesNotExist_nothingHappens() {
        // Arrange
            String fileName = "library";
            String GUID = "1064A";
            Book book = new Book("Name", "Author", "Category", "Language", LocalDate.parse("2000-10-10"), 100, GUID);
            String wrongGUID = "1001";
            // adding book to the library
            Tasks.saveDataToFile(book, fileName);
        // Act
            Tasks.removeBooksByGUID(wrongGUID, fileName);
            List<Book> list = Tasks.getListOfBooks();
            // clearing library
            Tasks.removeBooksByGUID(GUID, fileName);
        // Assert
            assertEquals(1, list.size());
    }

    /***
     * Testing method removeBooksByGUID(),
     * when library of books has a book and removal with existing GUID is done,
     * method removes a book from the library.
     */
    @Test
    void removeBooksByGUID_givenBookGUIDExist_removesBookByGUID() {
        // Arrange
            String fileName = "library";
            String GUID = "1064A";
            Book book = new Book("Name", "Author", "Category", "Language", LocalDate.parse("2000-10-10"), 100, GUID);
            Tasks.saveDataToFile(book, fileName);
        // Act
            Tasks.removeBooksByGUID(GUID, fileName);
        // Assert
            assertEquals(0, Tasks.getListOfBooks().size());
    }

    /***
     * Testing method takeBook(),
     * when reservation of book is not allowed (list is not empty), when the given book's
     * GUID is not existing - method returns a message about the operation.
     */
    @Test
    void takeBook_givenBookGUIDDoesNotExist_returnsMessage() {
        // Arrange
            String GUID = "1064A";
            Book book = new Book("Name", "Author", "Category", "Language", LocalDate.parse("2000-10-10"), 100, GUID);
            Tasks.saveDataToFile(book, "library");
            String wrongGUID = "1001";
            BookReservation reservation = new BookReservation("Person", 10, wrongGUID);
        // Act
            String message = Tasks.takeBook(reservation);
            Tasks.removeBooksByGUID(GUID, "library");
        // Assert
            assertEquals("Book does not exist. Try typing book's GUID again.", message);
    }

    /***
     * Testing method takeBook(),
     * when the book needed for person's reservation is already reserved
     * and another person wants to take it - method returns a message about the operation.
     */
    @Test
    void takeBook_givenBookIsAlreadyReserved_returnsMessage() {
        // Arrange
            String reservationsFileName = "reservations";
            String libraryFileName = "library";
            String GUID = "1064A";
            Book book = new Book("Name", "Author", "Category", "Language", LocalDate.parse("2000-10-10"), 100, GUID);
            Tasks.saveDataToFile(book, libraryFileName);
            BookReservation reservation = new BookReservation("Person", 10, GUID);
            Tasks.takeBook(reservation);
            BookReservation newReservation = new BookReservation("Person2", 15, GUID);
        // Act
            String message = Tasks.takeBook(newReservation);
            Tasks.removeBooksByGUID(GUID, libraryFileName);
            Tasks.removeBooksByGUID(GUID, reservationsFileName);
        // Assert
            assertEquals("Book is already reserved.", message);
    }

    /***
     * Testing method takeBook(),
     * when reservation of the books is allowed, and it is successfully taken,
     * method returns a message about the operation.
     */
    @Test
    void takeBook_givenBookIsSuccessfullyTaken_returnsMessage() {
        // Arrange
            String reservationsFileName = "reservations";
            String libraryFileName = "library";
            String GUID = "1064A";
            Book book = new Book("Name", "Author", "Category", "Language", LocalDate.parse("2000-10-10"), 100, GUID);
            Tasks.saveDataToFile(book, libraryFileName);
            BookReservation reservation = new BookReservation("Person", 10, GUID);
        // Act
            String message = Tasks.takeBook(reservation);
            Tasks.removeBooksByGUID(GUID, libraryFileName);
            Tasks.removeBooksByGUID(GUID, reservationsFileName);
        // Assert
            assertEquals("Book has been successfully taken.", message);
    }

    /***
     * Testing method takeBook(),
     * when reservation of the book is not allowed, because person has already
     * taken 3 books - method returns a message about the operation.
     */
    @Test
    void takeBook_personAlreadyHasTaken3Books_returnsMessage() {
        // Arrange
            String reservationsFileName = "reservations";
            String libraryFileName = "library";
            // adding books to the library
            Book book1 = new Book("Name1", "Author1", "Category", "Language", LocalDate.parse("2000-10-10"), 100, "1");
            Book book2 = new Book("Name2", "Author2", "Category", "Language", LocalDate.parse("2000-10-10"), 100, "2");
            Book book3 = new Book("Name3", "Author3", "Category", "Language", LocalDate.parse("2000-10-10"), 100, "3");
            Book book4 = new Book("Name4", "Author4", "Category", "Language", LocalDate.parse("2000-10-10"), 100, "4");
            Tasks.saveDataToFile(book1, libraryFileName);
            Tasks.saveDataToFile(book2, libraryFileName);
            Tasks.saveDataToFile(book3, libraryFileName);
            Tasks.saveDataToFile(book4, libraryFileName);
            // creating reservations
            Tasks.takeBook(new BookReservation("Person", 10, "1"));
            Tasks.takeBook(new BookReservation("Person", 15, "2"));
            Tasks.takeBook(new BookReservation("Person", 50, "3"));
            // when the same person want to have his 4th book
            BookReservation reservation = new BookReservation("Person", 5, "4");
        // Act
            String message = Tasks.takeBook(reservation);
            // clearing libraries
            Tasks.removeBooksByGUID("1", libraryFileName);
            Tasks.removeBooksByGUID("1", reservationsFileName);
            Tasks.removeBooksByGUID("2", libraryFileName);
            Tasks.removeBooksByGUID("2", reservationsFileName);
            Tasks.removeBooksByGUID("3", libraryFileName);
            Tasks.removeBooksByGUID("3", reservationsFileName);
            Tasks.removeBooksByGUID("4", libraryFileName);
        // Assert
            assertEquals("You have already taken 3 books.", message);
    }
}