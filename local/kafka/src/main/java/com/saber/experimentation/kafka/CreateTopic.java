package com.saber.experimentation.kafka;

import org.apache.kafka.clients.admin.Admin;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.CreateTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.KafkaFuture;

import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

import static com.saber.experimentation.kafka.CommonProperties.BOOT_SERVER;
import static com.saber.experimentation.kafka.CommonProperties.TEST_TOPIC_NAME;

public class CreateTopic {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        Properties properties = new Properties();
        properties.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, BOOT_SERVER);
        try (Admin admin = Admin.create(properties);) {
            NewTopic testTopic = new NewTopic(TEST_TOPIC_NAME, 30, (short) 1);
            CreateTopicsResult result = admin.createTopics(Collections.singleton(testTopic));
            KafkaFuture<Void> future = result.values().get(TEST_TOPIC_NAME);
            future.get();
        }
    }
}
