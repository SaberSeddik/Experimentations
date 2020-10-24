package io.saber.experimentation.monitor.servicea.controllers;

import com.microsoft.azure.servicebus.Message;
import com.microsoft.azure.servicebus.TopicClient;
import com.microsoft.azure.servicebus.primitives.ConnectionStringBuilder;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class CallServiceDOverAmqp {

    private static final String TOPIC_NAME = "from-a";

    @Value("${spring.jms.servicebus.connection-string}")
    private String connectionString;

    @SneakyThrows
    public void call() {
        LOGGER.info("Service A: Calling D server over amqp");
        TopicClient sendClient = new TopicClient(new ConnectionStringBuilder(connectionString, TOPIC_NAME));

        Message message = new Message("This is a message from A");
        message.setContentType("application/json");
        message.setLabel("messageFromA");
        message.setMessageId(UUID.randomUUID().toString());
        message.setTimeToLive(Duration.ofMinutes(2));
        message.getProperties().put("hello", "saber");
        LOGGER.info("Message sending: Id = {}", message.getMessageId());
        sendClient.send(message);
    }
}

