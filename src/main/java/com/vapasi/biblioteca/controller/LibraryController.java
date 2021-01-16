package com.vapasi.biblioteca.controller;

import com.vapasi.biblioteca.request.BookDto;
import com.vapasi.biblioteca.request.MovieDto;
import com.vapasi.biblioteca.request.UserDto;
import com.vapasi.biblioteca.response.*;
import com.vapasi.biblioteca.service.BookService;
import com.vapasi.biblioteca.service.MovieService;
import com.vapasi.biblioteca.service.UserAccountService;
import com.vapasi.biblioteca.util.Constants;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import static com.vapasi.biblioteca.util.Constants.*;

@RestController
@Api(value = "Biblioteca", tags = {"Biblioteca"})
@RequestMapping("/library-management")
public class LibraryController {

    Logger logger = LoggerFactory.getLogger(LibraryController.class);

    private BookService bookService;

    private MovieService movieService;

    private UserAccountService userAccountService;

    public LibraryController(BookService bookService, MovieService movieService, UserAccountService userAccountService) {
        this.bookService = bookService;
        this.movieService = movieService;
        this.userAccountService =  userAccountService;
    }

    @GetMapping
    @ApiOperation(value = "Returns a welcome message to user")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = Constants.WELCOME_MESSAGE, response = String.class)})
    public @ResponseBody String welcome() {
        logger.debug("In Book Controller: Welcome message");
        return bookService.returnWelcomeMessage();
    }

    @GetMapping("/books/listavailable")
    @ApiOperation(value = "Lists all books that are available for check out")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "List of all available books. Empty if none found", response = BookResponse.class)})
    public ResponseEntity<List<BookResponse>> getAllAvailable() {
        logger.info("In Book Controller: Display the list of available books in library");
        return ResponseEntity.ok(bookService.getAllBooks(true));
    }

    @GetMapping("/books/listunavailable")
    @ApiOperation(value = "Lists all books that are not available for check out")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "List of all unavailable books. Empty if none found", response = BookResponse.class)})
    public ResponseEntity<List<BookResponse>> getAllUnavailable() {
        logger.info("In Book Controller: Display the list of unavailable books in library");
        return ResponseEntity.ok(bookService.getAllBooks(false));
    }

    @PostMapping("/books/checkout")
    @ApiOperation(value = "Checkout a book that is available.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = CHECKOUT_SUCCESS, response = CheckoutResponse.class),
            @ApiResponse(code = 406, message = CHECKOUT_FAILURE, response = CheckoutResponse.class)
    })
    @ResponseBody
    public ResponseEntity<CheckoutResponse> checkoutBook(@ApiParam(
            name =  "BookDto",
            value = "Represents the JSON input to the checkout API",
            required = true) @RequestBody BookDto bookDto) throws URISyntaxException {
        logger.info("In Book Controller: Checkout an available book from the library");
        return ResponseEntity.created(new URI("/library-management/books/checkout")).body(bookService.checkoutBook(bookDto));

    }

    @PostMapping("/books/return")
    @ApiOperation(value = "Return a book that has been checked out.")
    @ResponseBody
    public ResponseEntity<ReturnResponse> returnBook(@ApiParam(
            name =  "BookDto",
            value = "Represents the JSON input to the return API",
            required = true) @RequestBody BookDto bookDto) throws URISyntaxException {
        logger.info("In Book Controller: Return a book to the library");
        return ResponseEntity.created(new URI("/library-management/books/return")).body(bookService.returnBook(bookDto));

    }

    @GetMapping("/movies")
    @ApiOperation(value = "Lists all movies that are available for check out")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "List of all available movies. Empty if none found", response = MovieResponse.class)})
    public ResponseEntity<List<MovieResponse>> getAllMovies() {
        logger.info("In Library Controller: Display the list of available movies in library");
        return ResponseEntity.ok(movieService.getAvailableMovies());
    }


    @PostMapping("/movies/checkout")
    @ApiOperation(value = "Checkout a movie that is available.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = MOVIE_CHECKOUT_SUCCESS, response = CheckoutResponse.class),
            @ApiResponse(code = 406, message = MOVIE_CHECKOUT_FAILURE, response = CheckoutResponse.class)
    })
    @ResponseBody
    public ResponseEntity<CheckoutResponse> checkoutMovie(@ApiParam(
            name =  "MovieDto",
            value = "Represents the JSON input to the Movie checkout API",
            required = true) @RequestBody MovieDto movieDto) throws URISyntaxException {
        logger.info("In Library Controller: Checkout an available movie from the library");
        return ResponseEntity.created(new URI("/library-management/movies/checkout")).body(movieService.checkoutMovie(movieDto));

    }

    @GetMapping("/user")
    @ApiOperation(value = "User details for a given library number.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "", response = UserAccountDetailResponse.class),
            @ApiResponse(code = 404, message = INVALID_LIBRARY_NUMBER, response = UserAccountDetailResponse.class)
    })
    @ResponseBody
    public ResponseEntity<UserAccountDetailResponse> getUserDetails(@ApiParam(
            name =  "librarynumber",
            value = "Represents the JSON input to the get user details API",
            required = true) @RequestParam("librarynumber") String librarynumber) throws URISyntaxException {

        logger.info("In Library Controller: Fetch user details for a given library number");
        return ResponseEntity.ok(userAccountService.getUserDetailsByLibraryNumber(librarynumber));

    }

    @ApiOperation(value = "Login into Biblioteca")
    @PostMapping("/login")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully authenticated user"),
            @ApiResponse(code = 403, message = "Invalid librarynumber or password")
    })
    public void login(@ApiParam(
            name =  "UserDto",
            value = "Represents the JSON input to the login API",
            required = true) @RequestBody UserDto userDto) {
        throw new IllegalStateException("This method shouldn't be called. It's implemented by Spring Security filters.");
    }

}

