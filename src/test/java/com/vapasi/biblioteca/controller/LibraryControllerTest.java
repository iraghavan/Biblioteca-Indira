package com.vapasi.biblioteca.controller;

import com.vapasi.biblioteca.exception.NotAValidBookException;
import com.vapasi.biblioteca.exception.NotAValidLibraryNumberException;
import com.vapasi.biblioteca.exception.NotAValidMovieException;
import com.vapasi.biblioteca.model.Book;
import com.vapasi.biblioteca.model.Movie;
import com.vapasi.biblioteca.model.UserAccount;
import com.vapasi.biblioteca.request.BookDto;
import com.vapasi.biblioteca.request.MovieDto;
import com.vapasi.biblioteca.request.UserDto;
import com.vapasi.biblioteca.response.*;
import com.vapasi.biblioteca.service.BookService;
import com.vapasi.biblioteca.service.MovieService;
import com.vapasi.biblioteca.service.UserAccountService;
import com.vapasi.biblioteca.util.Constants;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static com.vapasi.biblioteca.util.Constants.*;
import static org.mockito.ArgumentMatchers.any;


import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LibraryControllerTest {

    @MockBean
    private BookService bookService;
    @MockBean
    private MovieService movieService;
    @MockBean
    private UserAccountService userAccountService;
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext applicationContext;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
    }

    private static JSONObject apply(Book b) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject().put("isbn", b.getIsbn()).put("title", b.getTitle()).put("author", b.getAuthor()).
                    put("yearPublished", b.getPublished()).put("publisher", b.getPublisher()).put("imageurl", b.getImageurl());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    @Test
    public void shouldReturnWelcomeMessage() throws Exception {
        when(bookService.returnWelcomeMessage()).thenReturn(Constants.WELCOME_MESSAGE);
        this.mockMvc.perform(get("/library-management")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString(Constants.WELCOME_MESSAGE)));
    }

    @Test
    public void shouldListAllUncheckedoutBooks() throws Exception {
        Book bookA = new Book("isbnA","A","Author A",1990, "Publisher A", "image A", true);
        Book bookB = new Book("isbnB","B","Author B",1991, "Publisher A", "image A", true);
        Book bookD = new Book("isbnD","D","Author D",1993, "Publisher A", "image A", true);
        List<Book> allBooks = Arrays.asList( bookA,
                bookB,
                new Book("isbnC","C","Author C",1992, "Publisher A", "image A", false),
                bookD,
                new Book("isbnE","E","Author E",1994, "Publisher A", "image A", false));
        List<Book> unCheckedOutBooks = allBooks.stream().filter(b -> b.isAvailable()).collect(Collectors.toList());
        List<BookResponse> serviceResponse = unCheckedOutBooks.stream().map(b -> new BookResponse(b.getIsbn(), b.getTitle(), b.getAuthor(), b.getPublished(), b.getPublisher(), b.getImageurl())).collect(Collectors.toList());

        when(bookService.getAllUncheckedOutBooks()).thenReturn(serviceResponse);

        List<JSONObject> jsonObjects = unCheckedOutBooks.stream().map(LibraryControllerTest::apply).collect(Collectors.toList());

        JSONArray jsonArray = new JSONArray(jsonObjects);

        mockMvc.perform(MockMvcRequestBuilders.get("/library-management/books").
                contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).
                andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE)).
                andExpect(content().json(jsonArray.toString()));


    }

    @Test
    @WithMockUser(username = "111-2222", password = "1234 5678")
    public void shouldCheckoutBookSuccess() throws Exception {
        when(bookService.checkoutBook(any(BookDto.class))).thenReturn(new CheckoutResponse(CHECKOUT_SUCCESS));
        this.mockMvc.perform(MockMvcRequestBuilders.post("/library-management/books/checkout")
                .contentType(MediaType.APPLICATION_JSON).content("{\"isbn\": \"isbnE\"}").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json(new JSONObject().put("checkoutMessage", "Thank you! Enjoy the book").toString()));

    }

    @Test
    @WithMockUser(username = "111-2222", password = "1234 5678")
    public void shouldCheckoutBookFailure() throws Exception {
        when(bookService.checkoutBook(any(BookDto.class))).thenThrow(NotAValidBookException.class);
        this.mockMvc.perform(MockMvcRequestBuilders.post("/library-management/books/checkout")
                .contentType(MediaType.APPLICATION_JSON).content("{\"isbn\": \"isbnA\"}").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void shouldNotAllowCheckoutWithoutMockUser() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post("/library-management/books/checkout")
                .contentType(MediaType.APPLICATION_JSON).content("{\"isbn\": \"isbnA\"}").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }



    @Test
    @WithMockUser(username = "111-2222", password = "1234 5678")
    public void shouldReturnBookIfSuccessful() throws Exception {
        when(bookService.returnBook(any(BookDto.class))).thenReturn(new ReturnResponse(Constants.SUCCESSFUL_RETURN_MESSAGE));

        this.mockMvc.perform(MockMvcRequestBuilders.post("/library-management/books/return")
                .contentType(MediaType.APPLICATION_JSON).content("{\"isbn\": \"isbnA\"}").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(new JSONObject().put("returnMessage", Constants.SUCCESSFUL_RETURN_MESSAGE).toString()));
    }

    @Test
    @WithMockUser(username = "111-2222", password = "1234 5678")
    public void shouldNotReturnBookIfUnSuccessful() throws Exception {
        when(bookService.returnBook(any(BookDto.class))).thenThrow(NotAValidBookException.class);
        this.mockMvc.perform(MockMvcRequestBuilders.post("/library-management/books/return")
                .contentType(MediaType.APPLICATION_JSON).content("{\"isbn\": \"isbnA\"}").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void shouldNotAllowReturnWithoutMockUser() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post("/library-management/books/return")
                .contentType(MediaType.APPLICATION_JSON).content("{\"isbn\": \"isbnA\"}").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    private static JSONObject convertMovie(Movie m) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject().put("name", m.getName() ).put("director", m.getDirector())
                    .put("year", m.getYear()).
                    put("rating", m.getRating()).put("availableCopies", m.getAvailable());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    @Test
    public void shouldListAvailableMoviesWithCount() throws Exception {
        Movie movieA = new Movie("MovieA", "DirectorA", 2000, 2, 3, 10);
        Movie movieB = new Movie("MovieB", "DirectorB", 1999, 4, 0, 3);
        Movie movieD = new Movie("MovieD", "DirectorD", 1985, 1, 4, 5);
        List<Movie> allmovies = Arrays.asList( movieA,
                movieB,
                new Movie("MovieC", "DirectorC", 2020, 6, 3, 6),
                movieD);
        List<Movie> availableMovies = allmovies.stream().filter(b -> b.getAvailable() > 0).collect(Collectors.toList());
        List<MovieResponse> serviceResponse = availableMovies.stream().map(m -> new MovieResponse(m.getName(), m.getDirector(), m.getYear(), m.getRating(), m.getAvailable())).collect(Collectors.toList());

        when(movieService.getAvailableMovies()).thenReturn(serviceResponse);

        List<JSONObject> jsonObjects = availableMovies.stream().map(LibraryControllerTest::convertMovie).collect(Collectors.toList());

        JSONArray jsonArray = new JSONArray(jsonObjects);

        mockMvc.perform(MockMvcRequestBuilders.get("/library-management/movies").
                contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).
                andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE)).
                andExpect(content().json(jsonArray.toString()));
    }

    @Test
    public void shouldCheckoutMovieSuccess() throws Exception {
        when(movieService.checkoutMovie(any(MovieDto.class))).thenReturn(new CheckoutResponse(MOVIE_CHECKOUT_SUCCESS));
        this.mockMvc.perform(MockMvcRequestBuilders.post("/library-management/movies/checkout")
                .contentType(MediaType.APPLICATION_JSON).content("{\"name\": \"MovieA\"}").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json(new JSONObject().put("checkoutMessage", MOVIE_CHECKOUT_SUCCESS).toString()));

    }

    @Test
    public void shouldCheckoutMovieFailure() throws Exception {
        when(movieService.checkoutMovie(any(MovieDto.class))).thenThrow(NotAValidMovieException.class);
        this.mockMvc.perform(MockMvcRequestBuilders.post("/library-management/movies/checkout")
                .contentType(MediaType.APPLICATION_JSON).content("{\"name\": \"MovieA\"}").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotAcceptable());
    }

    @Test
    public void shouldListUserDetail() throws Exception {
        UserAccount user = new UserAccount("111-2222","1234 5678", "UserA", "user@abc.com", "1234567");
        UserAccountDetailResponse serviceResponse = new UserAccountDetailResponse(user.getLibrarynumber(), user.getName(), user.getEmail(),user.getPhone());

        when(userAccountService.getUserDetailsByLibraryNumber("111-2222")).thenReturn(serviceResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/library-management/user")
                .param("librarynumber", "111-2222")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON)).
                andExpect(content().json(convertUser(serviceResponse).toString()));

    }

    @Test
    public void shouldShowErrorWhenInvalidLibraryNumber() throws Exception {
        when(userAccountService.getUserDetailsByLibraryNumber("111-2222")).thenThrow(NotAValidLibraryNumberException.class);
        this.mockMvc.perform(MockMvcRequestBuilders.get("/library-management/user")
                .param("librarynumber", "111-2222")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

    }

    private static JSONObject convertUser(UserAccountDetailResponse ua) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject().put("librarynumber", ua.getLibrarynumber())
                    .put("name", ua.getName() )
                    .put("email", ua.getEmail())
                    .put("phone", ua.getPhone());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}
