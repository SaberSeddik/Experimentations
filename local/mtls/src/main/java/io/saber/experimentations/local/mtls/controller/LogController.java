package io.saber.experimentations.local.mtls.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Profile("server")
@RestController
@Slf4j
@RequiredArgsConstructor
public class LogController {

    @GetMapping("/log/{message}")
    public String log(@PathVariable(name = "message") String message) {
        LOGGER.info("This the message: '%s'".formatted(message));
        return "message sent";
    }
}
