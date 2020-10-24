package io.saber.experimentation.monitor.serviced.controllers;


import com.microsoft.azure.servicebus.*;
import com.microsoft.azure.servicebus.primitives.ConnectionStringBuilder;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.nio.charset.StandardCharsets.UTF_8;

@Component
@Slf4j
public class TopicReceiveController {

    private static final String TOPIC_NAME = "from-a";
    private static final String SUBSCRIPTION_NAME = "to-d";

    @Value("${spring.jms.servicebus.connection-string}")
    private String connectionString;


    @SneakyThrows
    @PostConstruct
    public void registerListener() {
        ExecutorService executorService = Executors.newCachedThreadPool();
        SubscriptionClient receiveClient = new SubscriptionClient(new ConnectionStringBuilder(connectionString,
                TOPIC_NAME + "/subscriptions/" + SUBSCRIPTION_NAME), ReceiveMode.PEEKLOCK);
        receiveClient.registerMessageHandler(new IMessageHandler() {
            @Override
            public CompletableFuture<Void> onMessageAsync(IMessage message) {
                LOGGER.info("Received message: {}", new String(message.getBody(), UTF_8));
                return receiveClient.completeAsync(message.getLockToken());
            }

            @Override
            public void notifyException(Throwable exception, ExceptionPhase exceptionPhase) {
                LOGGER.error(exceptionPhase.toString(), exception);
            }
        }, executorService);
    }
}