package com.todolist.app.service;

import com.todolist.app.exceptions.NotFoundException;
import com.todolist.app.models.TaskModel;
import com.todolist.app.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
public class TaskService {
    @Autowired
    TaskRepository taskRepository;

    public TaskModel saveTask(TaskModel task) {
        return taskRepository.save(task);
    }

    public Page<TaskModel> getAllTasks(Integer page, Integer size, Boolean enablePagination) {
        return taskRepository.findAll(enablePagination ? PageRequest.of(page, size) : Pageable.unpaged());
    }

    public TaskModel getTaskById(UUID id) {
        if (!taskRepository.existsById(id)) {
            throw new NotFoundException("Task not found", HttpStatus.NOT_FOUND);
        } else {
            return taskRepository.findById(id).get();
        }
    }

    public TaskModel updateTask(UUID id, TaskModel task) {
        if (!taskRepository.existsById(id)) {
            throw new NotFoundException("Task not found", HttpStatus.NOT_FOUND);
        } else {
            task.setId(id);
            return taskRepository.save(task);
        }
    }

    public void deleteTask(UUID id) {
        if (!taskRepository.existsById(id)) {
            throw new NotFoundException("Task not found", HttpStatus.NOT_FOUND);
        } else {
            taskRepository.deleteById(id);
        }
    }

    public boolean existsById(UUID id) {
        return taskRepository.existsById(id);
    }

}
