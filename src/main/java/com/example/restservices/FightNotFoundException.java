package com.example.restservices;

public class FightNotFoundException extends RuntimeException {
    FightNotFoundException(Long id) {
        super("Could not find fight " + id);
    }
}
