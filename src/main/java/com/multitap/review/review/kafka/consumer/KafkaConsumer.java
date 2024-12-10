package com.multitap.review.review.kafka.consumer;

import com.multitap.review.review.kafka.consumer.messagein.MentoringDataDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaConsumer {

    private final KafkaConsumerService kafkaConsumerService;

    @KafkaListener(topics = "mentoring-data", containerFactory = "mentoringDtoListener")
    public void processMentoringData(MentoringDataDto mentoringDataDto) {
        kafkaConsumerService.getMentoringData(mentoringDataDto);
    }
}
