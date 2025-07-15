package com.example.spring_boot.exception;

public class EmployeeNotFoundException extends RuntimeException {

    public EmployeeNotFoundException(String tag) {
        super("Could not find employee " + tag);
    }
}