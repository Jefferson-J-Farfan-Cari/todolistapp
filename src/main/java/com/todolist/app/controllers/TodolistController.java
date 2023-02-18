package com.todolist.app.controllers;

import com.todolist.app.models.TodolistModel;
import com.todolist.app.models.UserModel;
import com.todolist.app.requestbodies.TodolistRB;
import com.todolist.app.service.TodolistService;
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
@RequestMapping("/api/todolist")
public class TodolistController {
    @Autowired
    TodolistService todolistService;
    @Autowired
    UserService userService;

    @PostMapping()
    public TodolistModel saveTodolist(@RequestBody TodolistRB todolist) {
        UserModel user = userService.getUserById(todolist.getUser());
        TodolistModel auxTD = todolist.convert();
        auxTD.setUser(user);
        return this.todolistService.saveTodolist(auxTD);
    }

    @GetMapping()
    public Page<TodolistModel> getAllTodolist(
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size,
            @RequestParam(required = false, defaultValue = "false") Boolean enablePagination
    ) {
        return todolistService.getAllTodolist(page, size, enablePagination);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<TodolistModel> finTodolistById(@PathVariable UUID id) {
        return ResponseEntity.status(HttpStatus.OK).body(todolistService.getTodolistById(id));
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<TodolistModel> updateTodolist(@PathVariable UUID id, @RequestBody TodolistRB todolist) {
        UserModel user = userService.getUserById(todolist.getUser());
        TodolistModel auxTD = todolist.convert();
        auxTD.setUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(todolistService.updateTodolist(id, auxTD));
    }

    @PatchMapping(value = "/{id}")
    public ResponseEntity<TodolistModel> patchTodo(@PathVariable UUID id, @RequestBody Map<Object, Object> fields) {

        TodolistModel aux = todolistService.getTodolistById(id);

        if (aux != null) {
            fields.forEach((key, value) -> {
                Field field = ReflectionUtils.findField(TodolistModel.class, (String) key);
                assert field != null;
                field.setAccessible(true);
                if (!field.getName().equals("user"))
                    ReflectionUtils.setField(field, aux, value);
                else {
                    UserModel um = userService.getUserById(UUID.fromString(value.toString()));
                    ReflectionUtils.setField(field, aux, um);
                }
            });

            TodolistModel updateTodo = todolistService.saveTodolist(aux);
            return ResponseEntity.status(HttpStatus.CREATED).body(updateTodo);
        }

        return null;
    }

    @DeleteMapping(value = "/{id}")
    public void deleteTodolist(@PathVariable UUID id) {
        todolistService.deleteTodolist(id);
        ResponseEntity.ok(!todolistService.existsById(id));
    }

    @GetMapping(value = "/by_user/{id}")
    public Page<TodolistModel> findTodolistByUserId(
            @PathVariable UUID id,
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size,
            @RequestParam(required = false, defaultValue = "false") Boolean enablePagination
    ) {
        return todolistService.getTodoByUser(id, page, size, enablePagination);
    }

}
