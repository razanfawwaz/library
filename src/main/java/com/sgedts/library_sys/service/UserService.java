package com.sgedts.library_sys.service;

import com.sgedts.library_sys.bean.BCryptPasswordEncoder;
import com.sgedts.library_sys.bean.LoginBean;
import com.sgedts.library_sys.bean.UserBean;
import com.sgedts.library_sys.model.User;
import com.sgedts.library_sys.repository.UserRepository;
import com.sgedts.library_sys.security.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public User addUser(UserBean userBean) {
        if(userRepository.findByEmail(userBean.getEmail()) != null){
            throw new RuntimeException("Email already exists");
        }

        if(userRepository.findByUsername(userBean.getUsername()) != null){
            throw new RuntimeException("Username already exists");
        }

        User user = new User();
        user.setEmail(userBean.getEmail());
        user.setPassword(BCrypt.hashpw(userBean.getPassword(), BCrypt.gensalt()));
        user.setUsername(userBean.getUsername());
        return userRepository.save(user);
    }

    public User getUserById(Long id) {
        if (isUserExists(id)) {
            return userRepository.findById(id).get();
        }
        throw new RuntimeException("User not found");
    }

    public User updateUser(Long id, UserBean userBean) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            user.setEmail(userBean.getEmail());
            user.setPassword(userBean.getPassword());
            user.setUsername(userBean.getUsername());
            return userRepository.save(user);
        }
        throw new RuntimeException("User not found");
    }

    public User loginUser(LoginBean loginBean) {
        System.out.println(loginBean);
        User user = userRepository.findByUsername(loginBean.getUsername());
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        if (!BCrypt.checkpw(loginBean.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }
        return user;
    }

    private boolean isUserExists(Long id) {
        return userRepository.existsById(id);
    }
}
