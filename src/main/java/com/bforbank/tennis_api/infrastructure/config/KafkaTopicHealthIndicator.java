package com.bforbank.tennis_api.infrastructure.config;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.DescribeTopicsResult;
import org.apache.kafka.clients.admin.TopicDescription;
import org.apache.kafka.common.KafkaFuture;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

@Component
public class KafkaTopicHealthIndicator implements HealthIndicator {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    private static final String TOPIC = "match-results";

    @Override
    public Health health() {
        Properties props = new Properties();
        props.put("bootstrap.servers", bootstrapServers);
        try (AdminClient adminClient = AdminClient.create(props)) {
            DescribeTopicsResult result = adminClient.describeTopics(Collections.singletonList(TOPIC));
            KafkaFuture<Map<String, TopicDescription>> future = result.all();
            Map<String, TopicDescription> descriptions = future.get();
            if (descriptions.containsKey(TOPIC)) {
                return Health.up().withDetail("topic", TOPIC).build();
            } else {
                return Health.down().withDetail("topic", TOPIC).withDetail("error", "Topic not found").build();
            }
        } catch (InterruptedException | ExecutionException e) {
            return Health.down(e).withDetail("topic", TOPIC).build();
        }
    }
}
