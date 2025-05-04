//This is the Form Data for the project, which corresponds to the <form> section in index.html

package com.wimdeblauwe.examples.todomvcthymeleaf.todoitem.web;

import javax.validation.constraints.NotBlank;

public class TodoItemFormData {
    @NotBlank
    private String title;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        
    }
}
