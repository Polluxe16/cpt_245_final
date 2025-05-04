//This file, along with TodoItem, allows Todoitem entities to be stored in a database

package com.wimdeblauwe.examples.todomvcthymeleaf.todoitem;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.list;

public interface TodoItemRepository extends JpaRepository<TodoItem, Long> {
  int countAllByCompleted(boolean completed); 

  List<TodoItem> findAllByCompleted(boolean completed);


}
