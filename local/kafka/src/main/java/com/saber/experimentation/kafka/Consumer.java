package com.saber.experimentation.kafka;

import lombok.AllArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.saber.experimentation.kafka.CommonProperties.BOOT_SERVER;
import static com.saber.experimentation.kafka.CommonProperties.TEST_TOPIC_NAME;

public class Consumer {
    private static final String GROUP_NAME = "CONSUMER_GROUP";

    public static void main(String[] args) {
        ExecutorService service = Executors.newFixedThreadPool(3);
        service.submit(new ConsumerUnit("Consumer 1"));
        service.submit(new ConsumerUnit("Consumer 2"));
        service.submit(new ConsumerUnit("Consumer 3"));
    }

    @AllArgsConstructor
    public static class ConsumerUnit implements Runnable {
        private final String consumerName;

        @Override
        public void run() {
            Properties props = new Properties();
            props.put("bootstrap.servers", BOOT_SERVER);
            props.put("group.id", GROUP_NAME);
            props.put("key.deserializer",
                    "org.apache.kafka.common.serialization.StringDeserializer");
            props.put("value.deserializer",
                    "org.apache.kafka.common.serialization.StringDeserializer");
            KafkaConsumer<String, String> consumer =
                    new KafkaConsumer<String, String>(props);
            consumer.subscribe(Collections.singletonList(TEST_TOPIC_NAME));
            System.out.printf("Start consuming from %s\n", consumerName);
            Duration timeout = Duration.ofMillis(100);
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(timeout);
                for (ConsumerRecord<String, String> record : records) {
                    System.out.printf("consumer = %s, topic = %s, partition = %d, offset = %d, " +
                                    "customer = %s, country = %s\n",
                            consumerName, record.topic(), record.partition(), record.offset(),
                            record.key(), record.value());
                }
            }
        }
    }
}
