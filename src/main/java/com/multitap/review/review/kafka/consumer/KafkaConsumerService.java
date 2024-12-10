package com.multitap.review.review.kafka.consumer;

import com.multitap.review.review.kafka.consumer.messagein.MentoringDataDto;

public interface KafkaConsumerService {
    void getMentoringData(MentoringDataDto mentoringDataDto);
}
