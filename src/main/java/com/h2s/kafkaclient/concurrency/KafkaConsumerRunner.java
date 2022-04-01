package com.h2s.kafkaclient.concurrency;

import com.h2s.kafkaclient.model.RecordModel;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.ProgressIndicator;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;

import java.time.Duration;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedTransferQueue;

public class KafkaConsumerRunner implements Runnable {
    private final String topic;
    private ObservableList<RecordModel> record;
    private final BlockingQueue ackQueue;
    private final KafkaConsumer<String, String> consumer;
    private BooleanProperty isConnected = new SimpleBooleanProperty(false);
    private ProgressIndicator progressIndicator;

    public KafkaConsumerRunner(String topic, Properties properties, ObservableList<RecordModel> record, ProgressIndicator progressIndicator) {
        this.topic = topic;
        this.ackQueue = new LinkedTransferQueue<Map<TopicPartition, OffsetAndMetadata>>();
        this.consumer = new KafkaConsumer<>(properties);
        this.record = record;
        this.progressIndicator = progressIndicator;
    }

    @Override
    public void run() {
        // Subscribe topic
        this.consumer.subscribe(Collections.singletonList(this.topic));
        this.isConnected.set(true);
        // Consume Kafka Message
        while (true) {
            try {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(10));
                for (ConsumerRecord<String, String> it : records) {
                    record.add(new RecordModel( String.valueOf(it.partition()), String.valueOf(it.offset()), String.valueOf(new Date(it.timestamp())), it.value()));
                    progressIndicator.setVisible(false);
                }
                consumer.commitSync();

            } catch (Exception e) {
                e.printStackTrace();
                this.isConnected.set(false);
            }
        }
    }

    public boolean isOpen() {
        return isConnected.get();
    }
}
