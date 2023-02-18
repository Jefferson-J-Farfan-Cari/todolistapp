package com.todolist.app.service;

import com.todolist.app.exceptions.NotFoundException;
import com.todolist.app.models.TodolistModel;
import com.todolist.app.repository.TodolistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TodolistService {
    @Autowired
    TodolistRepository todolistRepository;

    public TodolistModel saveTodolist(TodolistModel todolist) {
        return todolistRepository.save(todolist);
    }

    public Page<TodolistModel> getAllTodolist(Integer page, Integer size, Boolean enablePagination) {
        return todolistRepository.findAll(enablePagination ? PageRequest.of(page, size) : Pageable.unpaged());
    }

    public TodolistModel getTodolistById(UUID id) {
        if (!todolistRepository.existsById(id)) {
            throw new NotFoundException("Todolist not found", HttpStatus.NOT_FOUND);
        } else {
            return todolistRepository.findById(id).get();
        }
    }

    public TodolistModel updateTodolist(UUID id, TodolistModel todolist) {
        if (!todolistRepository.existsById(id)) {
            throw new NotFoundException("Todolist not found", HttpStatus.NOT_FOUND);
        } else {
            todolist.setId(id);
            todolist.setTasks(todolistRepository.findById(id).get().getTasks());
            return todolistRepository.save(todolist);
        }
    }

    public void deleteTodolist(UUID id) {
        if (!todolistRepository.existsById(id)) {
            throw new NotFoundException("Todolist not found", HttpStatus.NOT_FOUND);
        } else {
            todolistRepository.deleteById(id);
        }
    }

    public boolean existsById(UUID id) {
        return todolistRepository.existsById(id);
    }

    public Page<TodolistModel> getTodoByUser(UUID user, Integer page, Integer size, Boolean enablePagination){
        return todolistRepository.findAllByUserId(user, enablePagination ? PageRequest.of(page, size) : Pageable.unpaged());
    }

}
