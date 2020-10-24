package io.saber.experimentation.monitor.servicec;

import io.saber.experimentation.YamlPropertySourceFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource(value = "classpath:service-c.yml", factory = YamlPropertySourceFactory.class)
public class ServiceC {
    public static void main(String[] args) {
        SpringApplication.run(ServiceC.class, args);
    }
}