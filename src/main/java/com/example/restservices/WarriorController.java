package com.example.restservices;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

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
    ResponseEntity<?> addWarrior(@RequestBody Warrior addWarrior) {
        EntityModel<Warrior> entityModel = assembler.toModel(repository.save(addWarrior));

        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @GetMapping("/warrior/{id}")
    EntityModel<Warrior> oneWarrior(@PathVariable Long id) {
        Warrior warrior = repository.findById(id).orElseThrow(() ->
                new WarriorNotFoundException(id));

        return assembler.toModel(warrior);
    }

    @PutMapping("/warrior/{id}")
    ResponseEntity<?> updateWarrior(@RequestBody Warrior newWarrior, @PathVariable Long id) {
        Warrior respawnWarrior = repository.findById(id)
                .map(warrior -> {
                    warrior.setFirstName(newWarrior.getFirstName());
                    warrior.setLastName(newWarrior.getLastName());
                    warrior.setRole(newWarrior.getRole());
                    return repository.save(warrior);
                })
                .orElseGet(() -> {
                    newWarrior.setId(id);
                    return repository.save(newWarrior);
                });

        EntityModel<Warrior> entityModel = assembler.toModel(respawnWarrior);

        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @DeleteMapping("/warrior/{id}")
    ResponseEntity<?> killWarrior(@RequestBody Warrior warrior, @PathVariable Long id) {
        repository.deleteById(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(warrior.getFirstName() + " defeated.");
    }
}
