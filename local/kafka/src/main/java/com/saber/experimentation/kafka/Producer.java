package com.saber.experimentation.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

import static com.saber.experimentation.kafka.CommonProperties.BOOT_SERVER;
import static com.saber.experimentation.kafka.CommonProperties.TEST_TOPIC_NAME;

public class Producer {
    public static void main(String[] args) {
        Properties kafkaProps = new Properties();
        kafkaProps.put("bootstrap.servers", BOOT_SERVER);
        kafkaProps.put("key.serializer",
                "org.apache.kafka.common.serialization.StringSerializer");
        kafkaProps.put("value.serializer",
                "org.apache.kafka.common.serialization.StringSerializer");
        System.out.println("Start producer");
        int i = 0;
        while (true) {
            var producer = new KafkaProducer<String, String>(kafkaProps);
            ProducerRecord<String, String> record =
                    new ProducerRecord<>(TEST_TOPIC_NAME, "key_" + i,
                            "message " + i);
            try {
                producer.send(record).get();
            } catch (Exception e) {
            }
            i++;
        }
    }
}