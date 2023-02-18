package com.todolist.app.service;

import com.todolist.app.exceptions.NotFoundException;
import com.todolist.app.models.UserModel;
import com.todolist.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    public UserModel saveUser(UserModel user) {
        String pwd = user.getPassword();
        user.setPassword(passwordEncoder.encode(pwd));
        return userRepository.save(user);
    }

    public Page<UserModel> getAllUsers(Integer page, Integer size, Boolean enablePagination) {
        return userRepository.findAll(enablePagination ? PageRequest.of(page, size) : Pageable.unpaged());
    }

    public UserModel getUserById(UUID id) {
        if (!userRepository.existsById(id)) {
            throw new NotFoundException("User not found", HttpStatus.NOT_FOUND);
        } else {
            return userRepository.findById(id).get();
        }
    }

    public UserModel updateUser(UUID id, UserModel user) {
        if (!userRepository.existsById(id)) {
            throw new NotFoundException("User not found", HttpStatus.NOT_FOUND);
        } else {
            user.setId(id);
            String pwd = user.getPassword();
            user.setPassword(passwordEncoder.encode(pwd));
            return userRepository.save(user);
        }
    }

    public void deleteUser(UUID id) {
        if (!userRepository.existsById(id)) {
            throw new NotFoundException("User not found", HttpStatus.NOT_FOUND);
        } else {
            userRepository.deleteById(id);
        }
    }

    public boolean existsById(UUID id) {
        return userRepository.existsById(id);
    }

    public String findOneByEmail(String email) {
        return userRepository.findOneByEmail(email).get().getNames();
    }

}
