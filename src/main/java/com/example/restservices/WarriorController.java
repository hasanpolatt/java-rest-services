package com.example.restservices;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class WarriorController {

    private final WarriorRepository repository;

    WarriorController(WarriorRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/warriors")
    List<Warrior> all() {
        return repository.findAll();
    }

    @PostMapping("/warriors")
    Warrior addWarrior(@RequestBody Warrior addWarrior) {
        return repository.save(addWarrior);
    }

    @GetMapping("/warrior/{id}")
    Warrior oneWarrior(@PathVariable Long id) {
        return repository.findById(id).orElseThrow(() -> new WarriorNotFoundException(id));
    }

    @PutMapping("/warrior/{id}")
    Warrior updateWarrior(@RequestBody Warrior newWarrior, @PathVariable Long id) {

        return repository.findById(id).map(warrior -> {
            warrior.setName(newWarrior.getName());
            warrior.setRole(newWarrior.getRole());
            return repository.save(warrior);
        })
                .orElseGet(() -> {
                    newWarrior.setId(id);
                    return repository.save(newWarrior);
                });
    }

    @DeleteMapping("/warrior/{id}")
    void killWarrior(@PathVariable Long id) {
        repository.deleteById(id);
    }

}
