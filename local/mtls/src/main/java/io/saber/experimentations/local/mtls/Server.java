package io.saber.experimentations.local.mtls;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Profile;

@Profile("server")
@SpringBootApplication
public class Server {
    static {
        System.setProperty("spring.profiles.active", "server");
    }

    public static void main(String[] args) {
        SpringApplication.run(Server.class, args);
    }
}
