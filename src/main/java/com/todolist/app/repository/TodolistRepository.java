package com.todolist.app.repository;

import com.todolist.app.models.TodolistModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TodolistRepository extends JpaRepository<TodolistModel, UUID> {

    Page<TodolistModel> findAllByUserId(UUID id, Pageable pageable);
}
