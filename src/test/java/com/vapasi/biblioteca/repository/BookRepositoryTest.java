package com.vapasi.biblioteca.repository;

import com.vapasi.biblioteca.model.Book;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;


@DataJpaTest
@RunWith(SpringRunner.class)
public class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;
    private List<Book> allBooks;
    private List<Book> unCheckedOutBooks;

    @Test
    public void shouldListAllUnCheckedoutBooks() {
        allBooks = Arrays.asList( new Book("isbnA","A","Author A",1990, "Publisher A", "image A", true),
                new Book("isbnB","B","Author B",1991, "Publisher A", "image A", true),
                new Book("isbnC","C","Author C",1992, "Publisher A", "image A", false),
                new Book("isbnD","D","Author D",1993, "Publisher A", "image A", true),
                new Book("isbnE","E","Author E",1994, "Publisher A", "image A", false));
        unCheckedOutBooks = allBooks.stream().filter(b -> b.isAvailable()).collect(Collectors.toList());
        bookRepository.saveAll(allBooks);
        List<Book> books = bookRepository.findBooksOrderedByTitle(true);
        assertEquals(unCheckedOutBooks, books);
    }

    @Test
    public void shouldNotListAllcheckoutBooksWhenDatabaseIsEmpty(){
        assertEquals(new ArrayList<Book>(),bookRepository.findBooksOrderedByTitle(true));
    }

    @Test
    public void shouldNotListIfThereAreNoUncheckOutBooks() {
        allBooks = Arrays.asList( new Book("isbnA","A","Author A",1990, "Publisher A", "image A", false),
                new Book("isbnB","B","Author B",1991, "Publisher A", "image A", false),
                new Book("isbnC","C","Author C",1992, "Publisher A", "image A", false),
                new Book("isbnD","D","Author D",1993, "Publisher A", "image A", false),
                new Book("isbnE","E","Author E",1994, "Publisher A", "image A", false));
        bookRepository.saveAll(allBooks);
        List<Book> books = bookRepository.findBooksOrderedByTitle(true);
        assertEquals(new ArrayList<Book>(), books);
    }

    @Test
    public void shouldCheckoutBook() {
        allBooks = Arrays.asList( new Book("isbnA","A","Author A",1990, "Publisher A", "image A", false),
                new Book("isbnB","B","Author B",1991, "Publisher A", "image A", true),
                new Book("isbnC","C","Author C",1992, "Publisher A", "image A", true),
                new Book("isbnD","D","Author D",1993, "Publisher A", "image A", true),
                new Book("isbnE","E","Author E",1994, "Publisher A", "image A", true));
        bookRepository.saveAll(allBooks);

        Book checkedOutBook = bookRepository.findByIsbn("isbnC");
        checkedOutBook.setAvailable(false);
        bookRepository.save(checkedOutBook);
        assertFalse(bookRepository.findByIsbn("isbnC").isAvailable());
    }

    @Test
    public void shouldReturnBook() {
        Book book = new Book("isbnB","B","Author B",1991, "Publisher A", "image A", false);
        bookRepository.save(book);
        bookRepository.findByIsbn(book.getIsbn());
        book.setAvailable(true);
        bookRepository.save(book);
        assertTrue(book.isAvailable());
    }


}