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
    CommandLineRunner initDatabase(WarriorRepository repository) {
        return args -> {
            log.info("Preloading" + repository.save(new Warrior("Turin", "Turambar", "DragonSlayer")));
            log.info("Preloading" + repository.save(new Warrior("Tuor", "Eladar", "Savior of Noldor")));
            log.info("Preloading" + repository.save(new Warrior("Fingolfin", "Nolofinwe", "High King of Noldor")));
            log.info("Preloading" + repository.save(new Warrior("Feanor", "Curufinwe", "Creator of Silmarils")));
        };
    }
}
