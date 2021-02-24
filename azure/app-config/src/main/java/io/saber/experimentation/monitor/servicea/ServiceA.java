package io.saber.experimentation.monitor.servicea;

import com.azure.opentelemetry.exporters.azuremonitor.AzureMonitorExporter;
import com.azure.opentelemetry.exporters.azuremonitor.AzureMonitorExporterBuilder;
import io.saber.experimentation.YamlPropertySourceFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@PropertySource(value = "classpath:service-a.yml", factory = YamlPropertySourceFactory.class)
public class ServiceA {

    public static void main(String[] args) {
        SpringApplication.run(ServiceA.class, args);
    }

    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }
}
