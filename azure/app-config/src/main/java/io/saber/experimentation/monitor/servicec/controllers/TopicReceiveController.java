package io.saber.experimentation.monitor.servicec.controllers;


import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TopicReceiveController {
    private static final String TOPIC_NAME = "forward-topic";
    private static final String SUBSCRIPTION_NAME = "forward-topic";

    @JmsListener(destination = TOPIC_NAME, containerFactory = "topicJmsListenerContainerFactory",
            subscription = SUBSCRIPTION_NAME)
    public void receiveMessage(String message) {
        LOGGER.info("Received message: {}", message);
    }
}