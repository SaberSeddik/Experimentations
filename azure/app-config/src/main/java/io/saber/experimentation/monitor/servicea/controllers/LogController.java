package io.saber.experimentation.monitor.servicea.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.net.URISyntaxException;

@RestController
@Slf4j
@RequiredArgsConstructor
public class LogController {

    @Autowired
    private final CallServiceBOverHttp callServiceBOverHttp;

    @Autowired
    private final CallServiceDOverAmqp callServiceDOverAmqp;

    @GetMapping("/log/{message}")
    public String log(@PathVariable(name = "message") String message) throws URISyntaxException {
        LOGGER.info("Service A: This the message: '%s'".formatted(message));
        callServiceBOverHttp.call();
        callServiceDOverAmqp.call();
        return "message sent";
    }
}
