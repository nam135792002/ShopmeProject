package com.shopme.category;

public class CategoryNotFoundException extends Throwable {
    public CategoryNotFoundException(String message) {
        super(message);
    }
}
