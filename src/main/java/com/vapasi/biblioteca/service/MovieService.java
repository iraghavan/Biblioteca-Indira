package com.vapasi.biblioteca.service;

import com.vapasi.biblioteca.exception.NotAValidMovieException;
import com.vapasi.biblioteca.model.Movie;
import com.vapasi.biblioteca.request.MovieDto;
import com.vapasi.biblioteca.response.CheckoutResponse;
import com.vapasi.biblioteca.response.MovieResponse;
import com.vapasi.biblioteca.repository.MovieRepository;
import com.vapasi.biblioteca.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MovieService {

    Logger logger = LoggerFactory.getLogger(MovieService.class);

    private MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public List<MovieResponse> getAvailableMovies() {
        logger.debug("In MovieService.getAvailableMovies : list of all available movies order by rating");
        List<Movie> availableMovies = movieRepository.findAllMoviesOrderedByRating();
        return availableMovies.stream().map(m -> new MovieResponse(m.getName(),m.getDirector(),m.getYear(),m.getRating(),m.getAvailable())).collect(Collectors.toList());
    }

    @Transactional
    public CheckoutResponse checkoutMovie(MovieDto movieDto) {
        logger.debug("In MovieService.checkoutMovie: check out any available movie");

        String checkoutResponse = Constants.MOVIE_CHECKOUT_FAILURE;
        Movie movieToCheckout = movieRepository.findByName(movieDto.getName());

        if (movieToCheckout != null && movieDto.getName() != null && movieToCheckout.getAvailable() > 0) {
            movieRepository.updateMovieAvailableCount(movieDto.getName());
            checkoutResponse = Constants.MOVIE_CHECKOUT_SUCCESS;
        }
        checkIfFailureToThrowException(checkoutResponse);
        return new CheckoutResponse(checkoutResponse);
    }

    private void checkIfFailureToThrowException(String message){
        if (message.equals(Constants.MOVIE_CHECKOUT_FAILURE)) {
            logger.error("Invalid Movie selected:"+ message);
            throw new NotAValidMovieException(
                    message);
        }
    }
}
