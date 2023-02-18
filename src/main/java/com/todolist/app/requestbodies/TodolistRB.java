package com.todolist.app.requestbodies;

import com.todolist.app.models.TodolistModel;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class TodolistRB {

    private String title;
    private String description;
    private LocalDateTime end_date;
    private UUID user;

    public TodolistModel convert() {
        TodolistModel aux = new TodolistModel();
        aux.setTitle(this.title);
        aux.setDescription(description);
        aux.setEnd_date(end_date);
        return aux;
    }

}
