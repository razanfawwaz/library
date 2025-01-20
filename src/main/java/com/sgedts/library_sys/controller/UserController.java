package com.sgedts.library_sys.controller;

import com.sgedts.library_sys.bean.ApiResponse;
import com.sgedts.library_sys.bean.LoginBean;
import com.sgedts.library_sys.bean.UserBean;
import com.sgedts.library_sys.model.User;
import com.sgedts.library_sys.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<?> addUser(@Valid @RequestBody(required = false) UserBean userBean, BindingResult bindingResult) {
        if (userBean == null) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "Request body is required", null));
        }

        // Check validation errors
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error ->
                    errors.put(error.getField(), error.getDefaultMessage())
            );
            return ResponseEntity.badRequest().body(new ApiResponse(false, "Validation failed", errors));
        }

        try {
            // Proceed if validation passes
            userService.addUser(userBean);
            return ResponseEntity.ok(new ApiResponse(true, "User added successfully", null));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage(), null));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        try {
            User user = userService.getUserById(id);
            return ResponseEntity.ok(new ApiResponse(true, "User found", user));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage(), null));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginBean loginBean) {
        try {
            User user = userService.loginUser(loginBean);
            user.setPassword("");
            return ResponseEntity.ok(new ApiResponse(true, "User logged in successfully", user));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage(), null));
        }
    }
}
