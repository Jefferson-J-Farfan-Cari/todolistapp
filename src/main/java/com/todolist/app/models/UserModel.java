package com.todolist.app.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@Table(name = "user_user")
@EntityListeners(AuditingEntityListener.class)
public class UserModel {

    @Id
    @GeneratedValue(generator = "UUID2")
    @GenericGenerator(name = "UUID2", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", columnDefinition = "UUID")
    @JsonProperty(value = "id", access = JsonProperty.Access.READ_ONLY, index = 0)
    private UUID id;

    @Column(name = "username", length = 180, unique = true)
    private String username;

    @Column(name = "names", length = 180, nullable = false)
    private String names;

    @Column(name = "father_lastname", length = 180, nullable = false)
    private String father_lastname;

    @Column(name = "mother_lastname", length = 180)
    private String mother_lastname;

    @Column(name = "email", length = 180, nullable = false, unique = true)
    private String email;

    @Column(name = "password", length = 180, nullable = false)
    @JsonProperty(value = "password", access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Fetch(value = FetchMode.SELECT)
    @JsonProperty(value = "todolist", access = JsonProperty.Access.READ_ONLY)
    private List<TodolistModel> todolist = new ArrayList<>();

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
