package io.saber.experimentation.monitor.serviced;

import io.saber.experimentation.YamlPropertySourceFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource(value = "classpath:service-d.yml", factory = YamlPropertySourceFactory.class)
public class ServiceD {
    public static void main(String[] args) {
        SpringApplication.run(ServiceD.class, args);
    }
}