package com.vapasi.biblioteca.repository;

import com.vapasi.biblioteca.model.Movie;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;

@DataJpaTest
@RunWith(SpringRunner.class)
public class MovieRepositoryTest {

    @Autowired
    private MovieRepository movieRepository;
    private List<Movie> allMovies;

    @Test
    public void shouldListAllMoviesWithCount() {
        allMovies = Arrays.asList(new Movie("MovieA", "DirectorA", 2000, 1, 2, 1),
                new Movie("MovieB", "DirectorB", 2001, 2, 2, 3),
                new Movie("MovieC", "DirectorC", 2002, 3, 3, 5));
        movieRepository.saveAll(allMovies);
        List<Movie> movies = movieRepository.findAllMoviesOrderedByRating();
        assertEquals(allMovies, movies);
    }

    @Test
    public void shouldCheckoutMovies() {
        Movie movie = new Movie("MovieB", "DirectorB", 2001, 2, 2, 3);
        movieRepository.save(movie);
        Movie checkdoutMovie = movieRepository.findByName(movie.getName());
        movieRepository.updateMovieAvailableCount(checkdoutMovie.getName());
        assertEquals(Optional.of(1), Optional.of(movieRepository.findByName(movie.getName()).getAvailable()));
    }

    @Test
    public void shouldNotCheckoutMovieIfAvailableIsZero() {
        Movie movie = new Movie("MovieB", "DirectorB", 2001, 2, 0, 3);
        movieRepository.save(movie);
        Movie checkdoutMovie = movieRepository.findByName(movie.getName());
        movieRepository.updateMovieAvailableCount(checkdoutMovie.getName());
        assertEquals(Optional.of(0), Optional.of(movieRepository.findByName(movie.getName()).getAvailable()));
    }
}
