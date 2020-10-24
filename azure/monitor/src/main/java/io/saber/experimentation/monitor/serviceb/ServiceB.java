package io.saber.experimentation.monitor.serviceb;

import io.saber.experimentation.YamlPropertySourceFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource(value = "classpath:service-b.yml", factory = YamlPropertySourceFactory.class)
public class ServiceB {
    public static void main(String[] args) {
        SpringApplication.run(ServiceB.class, args);
    }
}
