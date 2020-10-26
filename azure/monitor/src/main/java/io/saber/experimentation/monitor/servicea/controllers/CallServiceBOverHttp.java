package io.saber.experimentation.monitor.servicea.controllers;

import io.opentelemetry.extensions.auto.annotations.WithSpan;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;

@Component
@Slf4j
@RequiredArgsConstructor
public class CallServiceBOverHttp {
    @Autowired
    private final RestTemplate restTemplate;

    @WithSpan
    public void call() throws URISyntaxException {
        LOGGER.info("Service A: Calling B server over http");
        URI uri = new URI("http://localhost:8082/log/from-serviceA-to-serviceB");
        restTemplate.getForObject(uri, String.class);
    }
}
