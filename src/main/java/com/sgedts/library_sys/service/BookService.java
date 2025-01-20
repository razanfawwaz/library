package com.sgedts.library_sys.service;

import com.sgedts.library_sys.model.Book;
import com.sgedts.library_sys.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.sgedts.library_sys.bean.BookBean;

import java.util.List;

@Service
public class BookService {
    @Autowired
    private BookRepository bookRepository;

    public Book addBook(BookBean bookBean){
        // check validation
        Book book = new Book();
        book.setTitle(bookBean.getTitle());
        book.setAuthor(bookBean.getAuthor());
        book.setQuantity(bookBean.getQuantity());
        return bookRepository.save(book);
    }

    public List<Book> getAllBooks(){
        if (bookRepository.findAll().isEmpty()){
            throw new RuntimeException("No books found");
        }

        return bookRepository.findAll();
    }

    public Book getBookById(Long id){
       if (isBookExists(id)){
           return bookRepository.findById(id).get();
       }
         throw new RuntimeException("Book not found");
    }

    public Book updateBook(Long id, BookBean bookBean){
        Book book = bookRepository.findById(id).orElse(null);
        if(book != null){
            book.setTitle(bookBean.getTitle());
            book.setAuthor(bookBean.getAuthor());
            book.setQuantity(bookBean.getQuantity());
            return bookRepository.save(book);
        }
        throw new RuntimeException("Book not found");
    }

    public String deleteBook(Long id){
        if(isBookExists(id)){
            bookRepository.deleteById(id);
            return "Book deleted successfully";
        }

        throw new RuntimeException("Book not found");
    }

    private boolean isBookExists(Long id){
        return bookRepository.findById(id).isPresent();
    }

}
