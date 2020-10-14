package io.saber.experimentation.monitor.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class LogController {
    @GetMapping("/log/{message}")
    public void log(@PathVariable(name = "message") String message) {
        LOGGER.info("This the message: '%s'".formatted(message));
    }
}
