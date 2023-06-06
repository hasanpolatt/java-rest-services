package com.example.restservices;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class LoadDatabase {

    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    @Bean
    CommandLineRunner initDatabase(WarriorRepository warriorRepository, FightRepository fightRepository) {
        return args -> {
            warriorRepository.save(new Warrior("Turin", "Turambar", "DragonSlayer"));
            warriorRepository.save(new Warrior("Tuor", "Eladar", "Savior of Noldor"));
            warriorRepository.save(new Warrior("Fingolfin", "Nolofinwe", "High King of Noldor"));
            warriorRepository.save(new Warrior("Feanor", "Curufinwe", "Creator of Silmarils"));

            warriorRepository.findAll().forEach(warrior -> log.info("Preloaded " + warrior));

            fightRepository.save(new Fight("The War of Wrath", Status.COMPLETED));
            fightRepository.save(new Fight("Battle of Unnumbered Tears", Status.FIGHTING));

            fightRepository.findAll().forEach(figth -> log.info("Preloaded " + figth));
        };
    }
}
