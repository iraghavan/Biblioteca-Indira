package com.vapasi.biblioteca.service;

import com.vapasi.biblioteca.exception.NotAValidBookException;
import com.vapasi.biblioteca.exception.NotAValidMovieException;
import com.vapasi.biblioteca.model.Book;
import com.vapasi.biblioteca.model.Movie;
import com.vapasi.biblioteca.repository.MovieRepository;
import com.vapasi.biblioteca.request.BookDto;
import com.vapasi.biblioteca.request.MovieDto;
import com.vapasi.biblioteca.response.CheckoutResponse;
import com.vapasi.biblioteca.response.MovieResponse;
import com.vapasi.biblioteca.util.Constants;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MovieServiceTest {

    @Mock
    private MovieRepository movieRepository;

    private MovieService movieService;

    @Before
    public void setUp() {
        movieService = new MovieService(movieRepository);
    }

    @Test
    public void shouldReturnAvailableMovie() {
        List<Movie> allMovies = Arrays.asList( new Movie("MovieA", "DirectorA",2000, 1,2,1),
                new Movie("MovieB","DirectorB",2001,2,2,1),
                new Movie("MovieC","DirectorC", 2002,null,3,0));
        List<Movie> availableMovies = allMovies.stream().filter(m -> m.getAvailable() > 0).collect(Collectors.toList());

        when(movieRepository.findAllMoviesOrderedByRating()).thenReturn(availableMovies);
        List<MovieResponse> actualResponse = movieService.getAvailableMovies();
        List<MovieResponse> expectedResponse = availableMovies.stream().map(m -> new MovieResponse(m.getName(),m.getDirector(),m.getYear(),m.getRating(),m.getAvailable())).collect(Collectors.toList());
        assertEquals("Not rated", allMovies.get(2).getRating());
        Assert.assertEquals(expectedResponse, actualResponse);

    }

    @Test
    public void shouldReturnSuccessMessageAfterCheckOut() {
        Movie movie = new Movie("MovieB","DirectorB",2001,2,2,1);
        MovieDto movieDto = new MovieDto("MovieB");
        when(movieRepository.findByName(movieDto.getName())).thenReturn(movie);
        CheckoutResponse actualResponse = movieService.checkoutMovie(movieDto);
        Assert.assertEquals(Constants.MOVIE_CHECKOUT_SUCCESS, actualResponse.getCheckoutMessage());
    }

   @Test(expected = NotAValidMovieException.class)
    public void shouldReturnFailureMessageIfMovieIsNotAvailable() {
        Movie movie = new Movie("MovieB","DirectorB",2001,2,2,1);
        MovieDto movieDto = new MovieDto("Spider");
        when(movieRepository.findByName(movieDto.getName())).thenReturn(null);
        movieService.checkoutMovie(movieDto);
    }

    @Test(expected = NotAValidMovieException.class)
    public void shouldReturnFailureMessageForCheckoutIfNameIsNull(){
         Movie movie = new Movie(null,"DirectorB",2001,2,2,1);
         MovieDto movieDto = new MovieDto(null);
         when(movieRepository.findByName(movieDto.getName())).thenReturn(null);
         movieService.checkoutMovie(movieDto);
     }

}
