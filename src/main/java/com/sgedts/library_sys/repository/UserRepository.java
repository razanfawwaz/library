package com.sgedts.library_sys.repository;

import com.sgedts.library_sys.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    // get user by email
    User findByEmail(String email);
    // get user by username
    User findByUsername(String username);
}
