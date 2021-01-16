package com.vapasi.biblioteca.repository;

import com.vapasi.biblioteca.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {

    @Query("select m from Movie m where m.available > 0 order by m.rating")
    List<Movie> findAllMoviesOrderedByRating();

    Movie findByName(String name);

    @Modifying(clearAutomatically = true)
    @Query("update Movie m set m.available = m.available - 1 where m.name = :name and m.available > 0")
    void updateMovieAvailableCount(@Param("name") String name);
}
