package com.bezkoder.springjwt.repository;

import com.bezkoder.springjwt.models.Books;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BooksRepository extends JpaRepository<Books, Long> {
//    Optional<Books> findByTitle(String title);
    Boolean existsByTitle(String title);
}
