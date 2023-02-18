package com.todolist.app.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Data
@Table(name = "task")
public class TaskModel {

    @Id
    @GeneratedValue(generator = "UUID2")
    @GenericGenerator(name = "UUID2", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", columnDefinition = "UUID")
    @JsonProperty(value = "id", access = JsonProperty.Access.READ_ONLY, index = 0)
    private UUID id;

    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @Column(name = "description", length = 180)
    private String description;

    @Column(name = "state", nullable = false)
    private boolean state = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "todolist_id")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private TodolistModel todolist;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty(value = "created_at", access = JsonProperty.Access.READ_ONLY)
    private Date created_at;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty(value = "updated_at", access = JsonProperty.Access.READ_ONLY)
    private Date updated_at;

    @PrePersist
    protected void prePersist() {
        if (this.created_at == null) created_at = new Date();
        if (this.updated_at == null) updated_at = new Date();
    }

    @PreUpdate
    protected void preUpdate() {
        this.updated_at = new Date();
    }

}
