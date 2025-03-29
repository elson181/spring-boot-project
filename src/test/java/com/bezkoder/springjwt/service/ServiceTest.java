package com.bezkoder.springjwt.service;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.bezkoder.springjwt.models.Books;
import com.bezkoder.springjwt.repository.BooksRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ServiceTest {

    @Mock
    private BooksRepository booksRepository; // Creates a mock instance of BookRepository

    @InjectMocks
    private TestService testService; // Injects the mock repository into BookService

    private Books books;

    @BeforeEach
    void setUp() {
        books = new Books(1L, "Mockito Guide", "123-456789", 2023, true, new HashSet<>(), new HashSet<>());
    }

    // ✅ Test case: getBookByTitle() returns a valid book
    @Test
    void testGetBookByTitle_WhenBookExists() {
        // Arrange
        when(booksRepository.findByTitle("Mockito Guide")).thenReturn(Optional.of(books));

        // Act
        Books foundBook = testService.getBookByTitle("Mockito Guide");

        // Assert
        assertNotNull(foundBook);
        assertEquals("Mockito Guide", foundBook.getTitle());
        verify(booksRepository, times(1)).findByTitle("Mockito Guide");
    }

    // ❌ Test case: getBookByTitle() throws exception if book is not found
    @Test
    void testGetBookByTitle_WhenBookNotFound() {
        // Arrange
        when(booksRepository.findByTitle("Unknown Book")).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class,
                () -> testService.getBookByTitle("Unknown Book"));

        assertEquals("Book not found", exception.getMessage());
        verify(booksRepository, times(1)).findByTitle("Unknown Book");
    }


}
