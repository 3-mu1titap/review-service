package com.multitap.review.review.application;

import com.multitap.review.common.entity.BaseResponseStatus;
import com.multitap.review.common.exception.BaseException;
import com.multitap.review.common.utils.ReviewUuidGenerator;
import com.multitap.review.review.dto.in.CreateReviewRequestDto;
import com.multitap.review.review.infrastructure.ReviewRepository;
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

        reviewRepository.save(createReviewRequestDto.toReview(reviewCode));
    }
}
