package com.example.restservices;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class WarriorController {

    private final WarriorRepository repository;
    private final WarriorModelAssembler assembler;

    WarriorController(WarriorRepository repository, WarriorModelAssembler assembler) {
        this.repository = repository;
        this.assembler = assembler;
    }

    @GetMapping("/warriors")
    CollectionModel<EntityModel<Warrior>> allWarriors() {
        List<EntityModel<Warrior>> warriors = repository.findAll().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(warriors, linkTo(methodOn(WarriorController.class).allWarriors()).withSelfRel());
    }

    @PostMapping("/warriors")
    Warrior addWarrior(@RequestBody Warrior addWarrior) {
        return repository.save(addWarrior);
    }

    @GetMapping("/warrior/{id}")
    EntityModel<Warrior> oneWarrior(@PathVariable Long id) {
        Warrior warrior = repository.findById(id).orElseThrow(() ->
                new WarriorNotFoundException(id));

        return assembler.toModel(warrior);
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
