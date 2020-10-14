package io.saber.experimentation.monitor.controllers;

import com.microsoft.applicationinsights.TelemetryClient;
import com.microsoft.applicationinsights.telemetry.Duration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class LogController {
    @Autowired
    TelemetryClient telemetryClient;

    @GetMapping("/log/{message}")
    public void log(@PathVariable(name = "message") String message) {
        //track a custom event
        telemetryClient.trackEvent("Sending a custom event...");
        //trace a custom trace
        telemetryClient.trackTrace("Sending a custom trace....");
        //track a custom metric
        telemetryClient.trackMetric("custom metric", 1.0);
        //track a custom dependency
        telemetryClient.trackDependency("SQL", "Insert", new Duration(0, 0, 1, 1, 1), true);

        LOGGER.info("This the message: '%s'".formatted(message));
    }
}
