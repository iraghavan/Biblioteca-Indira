package com.vapasi.biblioteca.repository;

import com.vapasi.biblioteca.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    @Query("select b from Book b where b.available = :available order by b.title asc")
    List<Book> findBooksOrderedByTitle(@Param("available") boolean available);

    Book findByIsbn(String isbnC);
}
