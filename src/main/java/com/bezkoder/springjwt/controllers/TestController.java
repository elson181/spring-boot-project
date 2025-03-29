package com.bezkoder.springjwt.controllers;

import com.bezkoder.springjwt.models.*;
import com.bezkoder.springjwt.service.TestService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/test")
public class TestController {

  @Autowired
  TestService testService;


  @GetMapping("/all")
  public String allAccess() {
    return "Public Content.";
  }

  @GetMapping("/circuitBreaker")
  public String circuitBreaker(){
    return testService.callPaymentService();
  }

  @GetMapping("/retryPattern")
  public String retryPattern(){
    return testService.checkInventory();
  }

  @GetMapping("/bulkHeadPattern")
  public String bulkHeadPattern() throws InterruptedException{
    return testService.processOrder();
  }

  @GetMapping("/allBooks")
  public List<Books> getAllBooks(){
    return testService.getAllBooks();
  }

  @GetMapping("/{title}")
  public ResponseEntity<Books> getBookByTitle(@PathVariable String title) {
    Books book = testService.getBookByTitle(title);
    return ResponseEntity.ok(book);
  }

  @GetMapping("/getBookById/{id}")
  @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
  public Books getBookById(@PathVariable Long id){
    return testService.getBookById(id);
  }

  @PostMapping("/addBook")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<Books> adddBook(@Valid @RequestBody Books books){
    Books addedBook =  testService.addBook(books);
    return new ResponseEntity<>(addedBook, HttpStatus.OK);
  }

  @DeleteMapping("/deleteBookById/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<Books> deleteBook(@PathVariable Long id){
    Books deletedBook = testService.deleteBookById(id);
    return new ResponseEntity<>(deletedBook, HttpStatus.OK);
  }

  @GetMapping("/user")
  @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
  public String userAccess() {
    return "User Content.";
  }

  @GetMapping("/mod")
  @PreAuthorize("hasRole('MODERATOR')")
  public String moderatorAccess() {
    return "Moderator Board.";
  }

  @GetMapping("/admin")
  @PreAuthorize("hasRole('ADMIN')")
  public String adminAccess() {
    return "Admin Board.";
  }

  // Get a specific borrow record by user and book
  @GetMapping("/getAllBorrowRecord")
  public List<BorrowRecord> getAllBorrowRecord() {
    return testService.getAllBorrowRecord();
  }

  // Get a specific borrow record by user and book
  @GetMapping("/users/{userId}/books/{bookId}/borrow-record")
  public BorrowRecord getBorrowRecord(@PathVariable Long userId, @PathVariable Long bookId) {
    return testService.getBorrowRecord(userId, bookId);
  }

  //Post Controllers
  @PostMapping("/addBookAndAuthor")
  public ResponseEntity<?> addBookAndAuthor(@RequestBody Books books) {
    System.out.println("controller");
    ResponseEntity<?> response = testService.addBookAndAuthor(books);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

}
