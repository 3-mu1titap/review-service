package com.multitap.review.review.kafka.consumer;

import com.multitap.review.common.utils.ReviewUuidGenerator;
import com.multitap.review.review.infrastructure.ReviewRepository;
import com.multitap.review.review.kafka.consumer.messagein.MentoringDataDto;
import com.multitap.review.review.kafka.producer.ReviewDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaConsumerServiceImpl implements KafkaConsumerService{

    private final ReviewRepository reviewRepository;

    @Override
    public void getMentoringData(MentoringDataDto mentoringDataDto) {
        log.info("세션uuid:{}", mentoringDataDto.getSessionUuid().get(0));
        String reviewCode = ReviewUuidGenerator.generateUniqueReviewCode("RV-");

        ReviewDto reviewDto =

    }
}
