package com.sgedts.library_sys.controller;

import com.sgedts.library_sys.bean.ApiResponse;
import com.sgedts.library_sys.bean.BookBean;
import com.sgedts.library_sys.model.Book;
import com.sgedts.library_sys.service.BookService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/books")
public class BookController {

    @Autowired
    private BookService bookService;

    @PostMapping
    public ResponseEntity<?> addBook(@Valid @RequestBody(required = false) BookBean bookBean, BindingResult bindingResult) {
        if (bookBean == null) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "Request body is required", null));
        }

        // Validate the fields in BookBean
        ResponseEntity<?> errors = getResponseEntity(bindingResult);
        if (errors != null) return errors;

        System.out.println(bindingResult);

        // Proceed if validation passes
        Book book = bookService.addBook(bookBean);
        return ResponseEntity.ok(new ApiResponse(true, "Book added successfully", book));
    }


    private ResponseEntity<?> getResponseEntity(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            // Map all validation errors
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error ->
                    errors.put(error.getField(), error.getDefaultMessage())
            );

            // Return errors in the response
            return ResponseEntity.badRequest().body(
                    new ApiResponse(false, "Validation failed", errors)
            );
        }
        return null;
    }

    @GetMapping
    public ResponseEntity<?> getAllBooks() {
        List<Book> books = bookService.getAllBooks();
        if (books.isEmpty()) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "No books found", null));
        }
        return ResponseEntity.ok(new ApiResponse(true, "Books found", books));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBookById(@PathVariable Long id) {
        try {
            Book book = bookService.getBookById(id);
            return ResponseEntity.ok(new ApiResponse(true, "Book found", book));
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, ex.getMessage(), null));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateBook(@PathVariable Long id, @Valid @RequestBody BookBean bookBean, BindingResult bindingResult) {
        ResponseEntity<?> errors = getResponseEntity(bindingResult);
        if (errors != null) return errors;
        try {
            Book book = bookService.updateBook(id, bookBean);
            return ResponseEntity.ok(new ApiResponse(true, "Book updated successfully", book));
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, ex.getMessage(), null));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBook(@PathVariable Long id) {
        try {
            bookService.deleteBook(id);
            return ResponseEntity.ok(new ApiResponse(true, "Book deleted successfully", null));
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, ex.getMessage(), null));
        }
    }
}
