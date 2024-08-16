package com.i2i.intern.pixcell;

import com.i2i.intern.pixcell.message.BalanceMessage;
import com.i2i.intern.pixcell.message.Message;
import com.i2i.intern.pixcell.message.NotificationMessage;
import com.i2i.intern.pixcell.message.UsageRecordMessage;
import com.i2i.intern.pixcell.message.WalletMessage; // WalletMessage eklendi
import com.i2i.intern.pixcell.serializer.BalanceMessageSerializer;
import com.i2i.intern.pixcell.serializer.UsageRecordMessageSerializer;
import com.i2i.intern.pixcell.serializer.NotificationMessageSerializer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

public class Publisher<T extends Message> {

    Producer<String, T> producer;

    public <U extends T> Producer<String, U> createProducer(String className) {
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, ConfigLoader.getProperty("kafka.url"));
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, className);

        return new KafkaProducer<>(properties);
    }
    public void createBalanceMessageProducer() {
        producer = createProducer(BalanceMessageSerializer.class.getName());
    }
    public void createUsageRecordMessageProducer() {
        producer = createProducer(UsageRecordMessageSerializer.class.getName());
    }
    public void createNotificationMessageProducer() {
        producer = createProducer(NotificationMessageSerializer.class.getName());
    }
    public void send(T message, String topicName) {
        if (producer != null) {
            producer.send(new ProducerRecord<>(topicName, "operation", message));
        }
    }
    public void close() {
        producer.close();
    }
}
