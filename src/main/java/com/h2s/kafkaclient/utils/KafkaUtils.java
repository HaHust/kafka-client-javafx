package com.h2s.kafkaclient.utils;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.KafkaAdminClient;
import org.apache.kafka.clients.admin.ListTopicsOptions;
import org.apache.kafka.clients.admin.ListTopicsResult;

import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ExecutionException;

public class KafkaUtils {
    public boolean isValidTopic(String topic) {
        return topic != null && !topic.isEmpty();
    }

    public boolean isValidKey(String key) {
        return key != null && !key.isEmpty();
    }

    public boolean isValidValue(String value) {
        return value != null && !value.isEmpty();
    }

    public boolean isValidMessage(String message) {
        return message != null && !message.isEmpty();
    }

    public boolean isValidPartition(String partition) {
        return partition != null && !partition.isEmpty();
    }

    public boolean isValidOffset(String offset) {
        return offset != null && !offset.isEmpty();
    }

    public boolean isValidGroupId(String groupId) {
        return groupId != null && !groupId.isEmpty();
    }

    public static Set<String> checkConnection(Properties properties) {
        try (AdminClient client = KafkaAdminClient.create(properties)) {
            ListTopicsResult topics = client.listTopics(new ListTopicsOptions().timeoutMs(2000));
            Set<String> names = topics.names().get();
            if (names.isEmpty()) {
                System.out.println("No topics found");
            } else {
                System.out.println("Topics found: " + names);
                return names;
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }
}
