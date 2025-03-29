package com.bezkoder.springjwt.service;

import com.bezkoder.springjwt.exceptions.BookNotFoundException;
import com.bezkoder.springjwt.models.Author;
import com.bezkoder.springjwt.models.Books;
import com.bezkoder.springjwt.models.BorrowRecord;
import com.bezkoder.springjwt.payload.response.MessageResponse;
import com.bezkoder.springjwt.repository.AuthorsRepository;
import com.bezkoder.springjwt.repository.BooksRepository;
import com.bezkoder.springjwt.repository.BorrowRecordRepository;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;

@Service
public class TestService {

    @Autowired
    BooksRepository booksRepository;

    @Autowired
    BorrowRecordRepository borrowRecordRepository;

    @Autowired
    AuthorsRepository authorsRepository;

    public List<Books> getAllBooks(){
        return booksRepository.findAll();
    }

    public Books getBookById(Long id){
        return booksRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("book id" + id + "not found"));
    }

    public Books getBookByTitle(String title) {
        return booksRepository.findByTitle(title)
                .orElseThrow(() -> new RuntimeException("Book not found"));
    }

    public Books deleteBookById(Long id){
        Books deletedBook = booksRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException("Book id" + id + "not found"));
        booksRepository.deleteById(id);
        return deletedBook;
    }

    public Books addBook(Books books){
        return booksRepository.save(books);
    }

//    public ResponseEntity<?> addBookAndAuthor(Books books) {
//        if(booksRepository.existsByTitle(books.getTitle())) {
//            return ResponseEntity
//                    .badRequest()
//                    .body(new MessageResponse("Book already exist"));
//        }
//        Set<Author> managedAuthors = new HashSet<>();
//        for (Author author : books.getAuthors()) {
//            Author managedAuthor = authorsRepository.findByName(author.getName())
//                    .orElseGet(() -> authorsRepository.save(new Author(author.getName())));
//            managedAuthors.add(managedAuthor);
//        }
//
//        books.setAuthors(managedAuthors);
//
//        // Save the book
//        booksRepository.save(books);
//        return ResponseEntity.ok(new MessageResponse("Book Added successfully!"));
//    }

    @Transactional
    public ResponseEntity<?> addBookAndAuthor(@Valid @RequestBody Books books) {
        System.out.println("service");
        // Check if the book already exists
        if (booksRepository.existsByTitle(books.getTitle())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Book already exists!"));
        }

        // Manage authors
        Set<Author> managedAuthors = new HashSet<>();
        for (Author author : books.getAuthors()) {
            // Find existing or save new author
            Author managedAuthor = authorsRepository.findByName(author.getName())
                    .orElseGet(() -> authorsRepository.save(new Author(author.getName())));
            managedAuthors.add(managedAuthor);
        }

        // Set the managed authors to the book
        books.setAuthors(managedAuthors);

        // Save the book
        Books savedBook = booksRepository.save(books);

        // Return a consistent response with book details
        return ResponseEntity.ok(savedBook);
    }


    // Method to get all borrow records for a specific user and book
    public BorrowRecord getBorrowRecord(Long userId, Long bookId) {
        return borrowRecordRepository.findAll()
                .stream()
                .filter(record -> record.getUser().getId().equals(userId) && record.getBook().getId().equals(bookId))
                .findFirst().orElseThrow(() -> new RuntimeException("No Borrow records"));
    }

    public List<BorrowRecord> getAllBorrowRecord(){
        return borrowRecordRepository.findAll();
    }

    private final RestTemplate restTemplate = new RestTemplate();

    @CircuitBreaker(name = "paymentService", fallbackMethod = "fallbackResponse")
    public String callPaymentService() {
        System.out.println("callPaymentService calling..");
        return restTemplate.getForObject("http://127.0.0.1:8080/api/test/all", String.class);
    }

    public String fallbackResponse(Exception e) {
        return "Payment service is currently unavailable. Please try again later.";
    }

    @Retry(name = "inventoryService", fallbackMethod = "fallbackResponse1")
    public String checkInventory() {
        System.out.println("checkInventory called");
        throw new RuntimeException("Service down");
    }

    public String fallbackResponse1(Exception e) {
        return "Inventory service is currently unavailable. Please try again later.";
    }

    @Bulkhead(name = "orderService", fallbackMethod = "fallbackResponse2")
    public String processOrder() throws InterruptedException {
        Thread.sleep(3000); // Simulate a delay of 3 seconds
        System.out.println("processOrder");
        return "✅ Order Processed Successfully!";
    }

    public String fallbackResponse2(Exception e) {
        System.out.println("Too many requests");
        return "⚠️ Too many requests, please try again later.";
    }



}
