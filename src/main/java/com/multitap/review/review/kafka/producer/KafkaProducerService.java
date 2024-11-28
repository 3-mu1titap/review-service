package com.multitap.review.review.kafka.producer;

public interface KafkaProducerService {

    //리뷰 조회 관련 메시지 전송
    void sendReview(ReviewDto reviewDto);
}
