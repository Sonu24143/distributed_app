package com.sonu.distributed;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.scheduling.annotation.EnableScheduling;

@Slf4j
@SpringBootApplication
@EnableScheduling
public class DistributedApplication implements ApplicationRunner {
	@Autowired
	private KafkaProducer<String, String> kafkaProducer;

	public static void main(String[] args) {
		SpringApplication.run(DistributedApplication.class, args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {

		try {
			log.info("Sending test message");
			kafkaProducer.send(new ProducerRecord<>("Hello-Kafka", "Test Message kafka producer"+System.currentTimeMillis()));
		} finally {
			kafkaProducer.close();
		}
	}

	@KafkaListener(topics = "Hello-Kafka", groupId = "test-consumer-group")
	public void listen(String message) {
		log.info("Consumed message [{}].", message);
	}
}
