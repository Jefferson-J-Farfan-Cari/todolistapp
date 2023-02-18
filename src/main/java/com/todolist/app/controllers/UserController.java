package com.todolist.app.controllers;

import com.todolist.app.models.UserModel;
import com.todolist.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    UserService userService;

    @PostMapping()
    public ResponseEntity<UserModel> saveUser(@RequestBody UserModel user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.saveUser(user));
    }

    @GetMapping
    public Page<UserModel> getAllUser(
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size,
            @RequestParam(required = false, defaultValue = "false") Boolean enablePagination
    ) {
        return userService.getAllUsers(page, size, enablePagination);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<UserModel> finUserById(@PathVariable UUID id) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getUserById(id));
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<UserModel> updateUser(@PathVariable UUID id, @RequestBody UserModel user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.updateUser(id, user));
    }

    @PatchMapping(value = "/{id}")
    public ResponseEntity<UserModel> patchUser(@PathVariable UUID id, @RequestBody Map<Object, Object> fields) {

        UserModel user = userService.getUserById(id);

        if (user != null) {
            fields.forEach((key, value) -> {
                Field field = ReflectionUtils.findField(UserModel.class, (String) key);
                assert field != null;
                field.setAccessible(true);
                ReflectionUtils.setField(field, user, value);
            });
            UserModel updatedUser = userService.saveUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(updatedUser);
        }

        return null;
    }

    @DeleteMapping(value = "/{id}")
    public void deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id);
        ResponseEntity.status(HttpStatus.OK).body(!userService.existsById(id));
    }
}