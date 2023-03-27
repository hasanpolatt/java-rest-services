package com.example.restservices;

public class WarriorNotFoundException extends RuntimeException {
    WarriorNotFoundException(Long id) {
        super("Not found any warrior "+ id);
    }
}
