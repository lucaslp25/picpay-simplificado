package com.lucasdev.picpaysimplificado.config;

import com.lucasdev.picpaysimplificado.model.entities.User;
import com.lucasdev.picpaysimplificado.model.enums.UserType;
import com.lucasdev.picpaysimplificado.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.util.List;

@Configuration
public class DataBasePopulationTest implements CommandLineRunner {

    private final UserRepository userRepository;

    public DataBasePopulationTest(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //increasing my database just for tests
    @Override
    public void run(String... args) throws Exception {

        userRepository.deleteAll();

        User user1 = new User(null, "John", "Smith", "045.612.070-07", "john@gmail.com", "secret", new BigDecimal(50), UserType.COMMOM);

        User user2 = new User(null, "alan", "walker", "708.585.110-45", "alan@gmail.com", "secret", new BigDecimal(50), UserType.COMMOM);

        User user3 = new User(null, "alex", "johnson", "951.354.500-84", "alex@gmail.com", "secret", new BigDecimal(50), UserType.MERCHANT);

        userRepository.saveAll(List.of(user1, user2, user3));
    }
}
