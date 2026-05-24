package com.example.comot.permission.application.exceptions;

import com.example.comot.permission.domaine.model.Category;

public class PermissionAlreadyExistsException extends RuntimeException {
    public PermissionAlreadyExistsException(Category category) {
        super("Permission with category " + category + " already exists");
    }
}
