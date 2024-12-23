package com.multitap.review.review.application;


import com.multitap.review.common.exception.BaseException;
import com.multitap.review.common.utils.ReviewUuidGenerator;
import com.multitap.review.review.domain.Review;
import com.multitap.review.review.dto.in.CreateReviewRequestDto;
import com.multitap.review.review.dto.in.SoftDeleteReviewRequestDto;
import com.multitap.review.review.dto.in.UpdateReviewRequestDto;
import com.multitap.review.review.infrastructure.ReviewRepository;
import com.multitap.review.review.kafka.producer.KafkaProducerService;
import com.multitap.review.review.kafka.producer.ReviewDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.multitap.review.common.entity.BaseResponseStatus.*;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final KafkaProducerService kafkaProducerService;

    @Override
    @Transactional
    public void createReview(CreateReviewRequestDto createReviewRequestDto) {
        log.info("Creating review {}", createReviewRequestDto);

        String reviewCode = ReviewUuidGenerator.generateUniqueReviewCode("RV-");

        if (reviewRepository.existsByMenteeUuidAndMentoringSessionUuid(
                createReviewRequestDto.getMenteeUuid(),
                createReviewRequestDto.getMentoringSessionUuid())) {
            throw new BaseException(ALREADY_WRITTEN_REVIEW);
        }

        ReviewDto reviewDto = ReviewDto.from(reviewRepository.save(createReviewRequestDto.toReview(reviewCode)));

        log.info("Review {}", reviewDto);

        kafkaProducerService.sendReview(reviewDto);
    }

    @Override
    @Transactional
    public void updateReview(UpdateReviewRequestDto updateReviewRequestDto) {
        log.info("Updating review {}", updateReviewRequestDto);

        Review updateReview = reviewRepository.findByReviewCodeAndMenteeUuid(updateReviewRequestDto.getReviewCode(), updateReviewRequestDto.getMenteeUuid())
                .orElseThrow(() -> new BaseException(REVIEW_NOT_FOUND));

        ReviewDto reviewDto = ReviewDto.from(reviewRepository.save(updateReviewRequestDto.updateReview(updateReview)));

        log.info("Review {}", reviewDto);

        kafkaProducerService.sendReview(reviewDto);
    }

    @Override
    @Transactional
    public void softDeleteReview(SoftDeleteReviewRequestDto softDeleteReviewRequestDto) {
        log.info("Deleting review {}", softDeleteReviewRequestDto);

        Review softDeleteReview = reviewRepository.findByReviewCodeAndMenteeUuid(
                softDeleteReviewRequestDto.getReviewCode(),
                        softDeleteReviewRequestDto.getMenteeUuid())
                .orElseThrow(() -> new BaseException(REVIEW_NOT_FOUND));

        ReviewDto reviewDto = ReviewDto.from(reviewRepository.save(softDeleteReviewRequestDto.SoftDeleteReview(softDeleteReview)));

        log.info("Review {}", reviewDto);

        kafkaProducerService.sendReview(reviewDto);
    }
}
