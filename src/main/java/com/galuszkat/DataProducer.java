package com.galuszkat;

import avro.CameraImage;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import javax.xml.crypto.Data;
import java.util.Calendar;
import java.util.Properties;

public class DataProducer {

    private static final String SCHEMA_REGISTRY_URL = "schema.registry.url";
    private static final String TOPIC = "laptop-images";
    private final Properties properties = new Properties();
    {
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "broker:9092");
        properties.put(ProducerConfig.ACKS_CONFIG, "1");
        properties.put(ProducerConfig.RETRIES_CONFIG, "10");
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class.getName());
        properties.put(SCHEMA_REGISTRY_URL, "http://127.0.0.1:8081");
    }
    private final KafkaProducer<String, CameraImage> kafkaProducer;

    public DataProducer() {
        this.kafkaProducer = new KafkaProducer<>(properties);
    }

    public void send(CameraImage image) {
        kafkaProducer.send(createRecord(image), (recordMetadata, e) -> {
            if (e == null) {
                System.out.println("Success");
                System.out.printf(recordMetadata.toString());
            } else {
                e.printStackTrace();
            }
        });
        kafkaProducer.flush();

    }

    public void close() {
        kafkaProducer.close();
    }

    private ProducerRecord<String, CameraImage> createRecord(CameraImage image) {
        return new ProducerRecord<>(TOPIC, String.valueOf(Calendar.getInstance().getTimeInMillis()), image);
    }
}
