package com.todolist.app.models;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@Table(name = "todolist")
public class TodolistModel {

    @Id
    @GeneratedValue(generator = "UUID2")
    @GenericGenerator(name = "UUID2", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", columnDefinition = "UUID")
    @JsonProperty(value = "id", access = JsonProperty.Access.READ_ONLY, index = 0)
    private UUID id;

    @Column(name = "title", length = 100, nullable = false)
    private String title;

    @Column(name = "description", length = 180)
    private String description;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "end_date", nullable = false)
    private LocalDateTime end_date;

    @Column(name = "finished", nullable = false)
    private boolean finished;

    @OneToMany(mappedBy = "todolist", cascade = CascadeType.ALL, orphanRemoval = true)
    @Fetch(value = FetchMode.SELECT)
    @JsonProperty(value = "tasks", access = JsonProperty.Access.READ_ONLY)
    private List<TaskModel> tasks = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private UserModel user;

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
