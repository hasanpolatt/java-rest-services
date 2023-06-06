package com.example.restservices;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.hateoas.mediatype.problem.Problem;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


import java.util.List;
import java.util.stream.Collectors;

public class FightController {

    private final FightRepository fightRepository;
    private final FightModelAssembler assembler;

    FightController(FightRepository fightRepository, FightModelAssembler assembler) {
        this.fightRepository = fightRepository;
        this.assembler = assembler;
    }

    @GetMapping("/fights")
    CollectionModel<EntityModel<Fight>> all() {

        List<EntityModel<Fight>> fights = fightRepository.findAll().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(fights,
                linkTo(methodOn(FightController.class).all()).withSelfRel());
    }

    @GetMapping("/fights/{id}")
    EntityModel<Fight> one(@PathVariable Long id) {
        Fight fight = fightRepository.findById(id)
                .orElseThrow(() -> new FightNotFoundException(id));

        return assembler.toModel(fight);
    }

    @PostMapping("/fights")
    ResponseEntity<EntityModel<Fight>> newFight(@RequestBody Fight fight) {
        fight.setStatus(Status.FIGHTING);
        Fight newFight = fightRepository.save(fight);

        return ResponseEntity
                .created(linkTo(methodOn(FightController.class).one(newFight.getId())).toUri())
                .body(assembler.toModel(newFight));
    }

    @PutMapping("/fights/{id}/completed")
    ResponseEntity<?> completed(@PathVariable Long id) {
        Fight fight = fightRepository.findById(id)
                .orElseThrow(() -> new FightNotFoundException(id));

        if (fight.getStatus() == Status.FIGHTING) {
            fight.setStatus(Status.COMPLETED);
            return ResponseEntity.ok(assembler.toModel(fightRepository.save(fight)));
        }

        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .header(HttpHeaders.CONTENT_TYPE, MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE)
                .body(Problem.create()
                        .withTitle("Method not allowed")
                        .withDetail("You can't over a fight that is in the " + fight.getStatus() + " status"));
    }

    @DeleteMapping("/fights/{id}/giveup")
    ResponseEntity<?> giveUp(@PathVariable Long id) {
        Fight fight = fightRepository.findById(id)
                .orElseThrow(() -> new FightNotFoundException(id));

        if (fight.getStatus() == Status.FIGHTING) {
            fight.setStatus(Status.RAN_AWAY);

            return ResponseEntity.ok(assembler.toModel(fightRepository.save(fight)));
        }

        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .header(HttpHeaders.CONTENT_TYPE,
                        MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE)
                .body(Problem.create()
                        .withTitle("Method not allowed")
                        .withDetail("YOu can't ran away a fight that is in the " + fight.getStatus() + " status"));
    }
}
