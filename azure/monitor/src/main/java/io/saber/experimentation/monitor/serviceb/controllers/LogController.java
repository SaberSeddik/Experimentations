package io.saber.experimentation.monitor.serviceb.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class LogController {
    private static final String DESTINATION_NAME = "test-topic";

    @Autowired
    private JmsTemplate jmsTemplate;

    @GetMapping("/log/{message}")
    public String log(@PathVariable(name = "message") String message) {
        LOGGER.info("Service B: This the message: '%s'".formatted(message));
        jmsTemplate.convertAndSend(DESTINATION_NAME, message);
        return "message sent";
    }
}
