package com.example.restservices;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import javax.swing.text.html.parser.Entity;

@Component
class FightModelAssembler implements RepresentationModelAssembler<Fight, EntityModel<Fight>> {

    @Override
    public EntityModel<Fight> toModel(Fight fight) {
        EntityModel<Fight> fightModel = EntityModel.of(fight,
                linkTo(methodOn(FightController.class).one(fight.getId())).withSelfRel(),
                linkTo(methodOn(FightController.class).all()).withRel("orders"));

        if (fight.getStatus() == Status.FIGHTING) {
            fightModel.add(linkTo(methodOn(FightController.class).giveUp(fight.getId())).withRel("cancel"));
            fightModel.add(linkTo(methodOn(FightController.class).completed(fight.getId())).withRel("complete"));
        }

        return fightModel;
    }
}
