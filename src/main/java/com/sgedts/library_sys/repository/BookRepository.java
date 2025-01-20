package com.sgedts.library_sys.repository;

import com.sgedts.library_sys.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {

}
