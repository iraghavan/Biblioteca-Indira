package com.vapasi.biblioteca.service;

import com.vapasi.biblioteca.exception.NotAValidBookException;
import com.vapasi.biblioteca.model.Book;
import com.vapasi.biblioteca.model.UserAccount;
import com.vapasi.biblioteca.repository.BookRepository;
import com.vapasi.biblioteca.request.BookDto;
import com.vapasi.biblioteca.response.BookResponse;
import com.vapasi.biblioteca.response.CheckoutResponse;
import com.vapasi.biblioteca.response.ReturnResponse;
import com.vapasi.biblioteca.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookService {
    Logger logger = LoggerFactory.getLogger(BookService.class);
    private BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public String returnWelcomeMessage() {
        return Constants.WELCOME_MESSAGE;
    }


    public List<BookResponse> getAllBooks(boolean available) {
        logger.debug("In BookService.getAllUncheckedOutBooks: list of all checked out books with order by name ASC");
        List<Book> unCheckedOutBooks = bookRepository.findBooksOrderedByTitle(available);
        if(unCheckedOutBooks == null || unCheckedOutBooks.size() == 0) {
            return null;
        }

        if (available) {
            return unCheckedOutBooks.stream().map(b -> new BookResponse(b.getIsbn(), b.getTitle(), b.getAuthor(), b.getPublished(), b.getPublisher(), b.getImageurl())).collect(Collectors.toList());
        }
        return unCheckedOutBooks.stream().map(b -> new BookResponse(b.getIsbn(), b.getTitle(), b.getAuthor(), b.getPublished(), b.getPublisher(), b.getImageurl(), b.getLastUserToCheckOut().getName())).collect(Collectors.toList());
    }

    public CheckoutResponse checkoutBook(BookDto bookDto) {
        logger.debug("In BookService.getAllUncheckedOutBooks: list of all checked out books with order by name ASC");

        String checkoutResponse = Constants.CHECKOUT_FAILURE;
        String isbn = bookDto.getIsbn();
        Book bookToCheckout = bookRepository.findByIsbn(isbn);

        if(bookToCheckout == null || bookDto.getIsbn() == null || !bookToCheckout.isAvailable()) {
            throw new NotAValidBookException(checkoutResponse);
        }

        bookToCheckout.setAvailable(false);
        String loggedInUser = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        bookToCheckout.setLastUserToCheckOut(new UserAccount(loggedInUser));
        bookRepository.save(bookToCheckout);
        return new CheckoutResponse(Constants.CHECKOUT_SUCCESS);
    }

    public ReturnResponse returnBook(BookDto bookDto) {
        logger.debug("In BookService.returnBooks: Return the book When it is checked out");
        String isbn = bookDto.getIsbn();
        Book bookToReturn = bookRepository.findByIsbn(isbn);

        if(bookToReturn == null || bookDto.getIsbn() == null) {
            throw new NotAValidBookException(
                    "No book found");

        }

        if(bookToReturn.isAvailable()) {
            throw new NotAValidBookException(
                    "Book is not checked out");

        }

        if(!bookToReturn.getLastUserToCheckOut().getLibrarynumber().equals(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString())) {
            throw new NotAValidBookException(
                    "User requesting return has not borrowed this book.");

        }

        bookToReturn.setAvailable(true);
        bookRepository.save(bookToReturn);
        return new ReturnResponse(Constants.SUCCESSFUL_RETURN_MESSAGE);
    }


}
