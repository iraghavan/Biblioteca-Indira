package com.vapasi.biblioteca.service;

import com.vapasi.biblioteca.exception.NotAValidBookException;
import com.vapasi.biblioteca.model.Book;
import com.vapasi.biblioteca.repository.BookRepository;
import com.vapasi.biblioteca.request.BookDto;
import com.vapasi.biblioteca.response.BookResponse;
import com.vapasi.biblioteca.response.CheckoutResponse;
import com.vapasi.biblioteca.response.ReturnResponse;
import com.vapasi.biblioteca.util.Constants;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;


import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    private BookService bookService;

    @Before
    public void setUp() {
        bookService = new BookService(bookRepository);
    }

    @Test
    public void shouldReturnWelcomeMessage() {
        assertEquals(Constants.WELCOME_MESSAGE,bookService.returnWelcomeMessage());
    }

    @Test
    public void shouldReturnListOfUncheckedOutBooks() {
        List<Book> allBooks = Arrays.asList( new Book("isbnA","A","Author A",1990, "Publisher A", "image A", true),
                new Book("isbnB","B","Author B",1991, "Publisher A", "image A", true),
                new Book("isbnC","C","Author C",1992, "Publisher A", "image A", false),
                new Book("isbnD","D","Author D",1993, "Publisher A", "image A", true),
                new Book("isbnE","E","Author E",1994, "Publisher A", "image A", false));
        List<Book> unCheckedOutBooks = allBooks.stream().filter(b -> !b.isAvailable()).collect(Collectors.toList());

        when(bookRepository.findBooksOrderedByTitle(true)).thenReturn(unCheckedOutBooks);
        List<BookResponse> actualResponse = bookService.getAllBooks(true);
        List<BookResponse> expectedResponse = unCheckedOutBooks.stream().map(b -> new BookResponse(b.getIsbn(), b.getTitle(), b.getAuthor(), b.getPublished(), b.getPublisher(), b.getImageurl())).collect(Collectors.toList());
        Assert.assertEquals(expectedResponse, actualResponse);

    }


    @Test
    public void shouldReturnEmptyListIfNoBooksAvailable() {
        when(bookRepository.findBooksOrderedByTitle(true)).thenReturn(Collections.EMPTY_LIST);
        List<BookResponse> actualResponse = bookService.getAllBooks(true);
        Assert.assertEquals(Collections.EMPTY_LIST, actualResponse);

    }

    @Test
    public void shouldReturnSuccessMessageAfterCheckOut() {
        Book book = new Book("isbnE","E","Author E",1994, "Publisher A", "image A", true);
        BookDto bookDto = new BookDto("isbnE");
        when(bookRepository.findByIsbn(bookDto.getIsbn())).thenReturn(book);
        CheckoutResponse actualResponse = bookService.checkoutBook(bookDto);
        Assert.assertEquals(Constants.CHECKOUT_SUCCESS, actualResponse.getCheckoutMessage());
    }

    @Test(expected = NotAValidBookException.class)
    public void shouldReturnFailureMessageIfBookIsNotAvailable() {
        Book book = new Book("isbnE","E","Author E",1994, "Publisher A", "image A", false);
        BookDto bookDto = new BookDto("isbnE");
        when(bookRepository.findByIsbn(bookDto.getIsbn())).thenReturn(book);
        bookService.checkoutBook(bookDto);
    }

    @Test(expected = NotAValidBookException.class)
    public void shouldReturnFailureMessageForCheckoutIfIsbnIsNull(){
        Book book = new Book("isbnE","E","Author E",1994, "Publisher A", "image A", true);
        BookDto bookDto = new BookDto(null);
        when(bookRepository.findByIsbn(bookDto.getIsbn())).thenReturn(null);
        bookService.checkoutBook(bookDto).getCheckoutMessage();
    }

    @Test
    public void shouldReturnBookWhenItIsSuccessful(){
        Book book = new Book("isbnE","E","Author E",1994, "Publisher A", "image A", false);
        BookDto bookDto = new BookDto("isbnE");
        when(bookRepository.findByIsbn(bookDto.getIsbn())).thenReturn(book);
        ReturnResponse actualResponse = bookService.returnBook(bookDto);
        Assert.assertEquals(Constants.SUCCESSFUL_RETURN_MESSAGE, actualResponse.getReturnMessage());
    }

    @Test(expected = NotAValidBookException.class)
    public void shouldNotReturnBookWhenItIsUnSuccessful(){
        Book book = new Book("isbnE","E","Author E",1994, "Publisher A", "image A", true);
        BookDto bookDto = new BookDto("isbnE");
        when(bookRepository.findByIsbn(bookDto.getIsbn())).thenReturn(book);
        bookService.returnBook(bookDto);
    }

    @Test(expected = NotAValidBookException.class)
    public void shouldReturnUnsuccessfulMessageIfIsbnIsNull() {
        Book book = new Book(null,"E","Author E",1994, "Publisher A", "image A", true);
        BookDto bookDto = new BookDto(null);
        when(bookRepository.findByIsbn(bookDto.getIsbn())).thenReturn(book);
        bookService.returnBook(bookDto);
    }

}