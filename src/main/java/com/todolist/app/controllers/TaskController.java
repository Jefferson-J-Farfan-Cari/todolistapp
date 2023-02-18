package com.todolist.app.controllers;

import com.todolist.app.models.TaskModel;
import com.todolist.app.models.TodolistModel;
import com.todolist.app.requestbodies.TaskRB;
import com.todolist.app.service.TaskService;
import com.todolist.app.service.TodolistService;
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
@RequestMapping("/api/tasks")
public class TaskController {
    @Autowired
    TaskService taskService;
    @Autowired
    TodolistService todolistRepository;

    @PostMapping()
    public TaskModel saveTask(@RequestBody TaskRB task) {
        TodolistModel todolist = todolistRepository.getTodolistById(task.getTodolist());
        TaskModel auxT = task.convert();
        auxT.setTodolist(todolist);
        return this.taskService.saveTask(auxT);
    }

    @GetMapping()
    public Page<TaskModel> getAllTasks(
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size,
            @RequestParam(required = false, defaultValue = "false") Boolean enablePagination
    ) {
        return taskService.getAllTasks(page, size, enablePagination);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<TaskModel> finTaskById(@PathVariable UUID id) {
        return ResponseEntity.status(HttpStatus.OK).body(taskService.getTaskById(id));
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<TaskModel> updateTask(@PathVariable UUID id, @RequestBody TaskRB task) {
        TodolistModel todolist = todolistRepository.getTodolistById(task.getTodolist());
        TaskModel auxT = task.convert();
        auxT.setTodolist(todolist);
        return ResponseEntity.status(HttpStatus.CREATED).body(taskService.updateTask(id, auxT));
    }

    @PatchMapping(value = "/{id}")
    public ResponseEntity<TaskModel> patchTask(@PathVariable UUID id, @RequestBody Map<Object, Object> fields) {

        TaskModel aux = taskService.getTaskById(id);

        if (aux != null) {
            fields.forEach((key, value) -> {
                Field field = ReflectionUtils.findField(TaskModel.class, (String) key);
                assert field != null;
                field.setAccessible(true);
                if (!field.getName().equals("todolist"))
                    ReflectionUtils.setField(field, aux, value);
                else {
                    TodolistModel tm = todolistRepository.getTodolistById(UUID.fromString(value.toString()));
                    ReflectionUtils.setField(field, aux, tm);
                }
            });

            TaskModel updateTask = taskService.saveTask(aux);
            return ResponseEntity.status(HttpStatus.CREATED).body(updateTask);
        }

        return null;
    }

    @DeleteMapping(value = "/{id}")
    public void deleteTask(@PathVariable UUID id) {
        taskService.deleteTask(id);
        ResponseEntity.ok(!taskService.existsById(id));
    }
}
