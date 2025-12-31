package com.back.infra.kafka;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
@EmbeddedKafka
public class KafkaTest {
    @Autowired
    private MyEventListener listener;

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Test
    @DisplayName("이벤트 수신 테스트")
    void t001() throws InterruptedException {
        MyEvent myEvent = new MyEvent("Hello, Kafka!");
        kafkaTemplate.send(myEvent.getEventName(), myEvent);

        boolean isReceived = listener.getLatch().await(10, TimeUnit.SECONDS);

        assertThat(isReceived).isTrue();
        assertThat(listener.getReceivedEvent().msg()).isEqualTo("Hello, Kafka!");
    }
}
