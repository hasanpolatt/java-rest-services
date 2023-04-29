package com.example.restservices;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
@Component
public class WarriorModelAssembler implements RepresentationModelAssembler<Warrior, EntityModel<Warrior>> {
    @Override
    public EntityModel<Warrior> toModel(Warrior warrior) {
        return EntityModel.of(warrior,
                linkTo(methodOn(WarriorController.class).oneWarrior(warrior.getId())).withSelfRel(),
                linkTo(methodOn(WarriorController.class).allWarriors()).withRel("warriors"));
    }
}
