package com.todolist.app.requestbodies;

import com.todolist.app.models.TaskModel;
import lombok.Data;

import java.util.UUID;

@Data
public class TaskRB {
    private String name;
    private String description;
    private Boolean state = null;
    private UUID todolist;

    public TaskModel convert(){
        TaskModel aux = new TaskModel();
        aux.setName(this.name);
        aux.setDescription(this.description);
        aux.setState(this.state);
        return aux;
    }

}
