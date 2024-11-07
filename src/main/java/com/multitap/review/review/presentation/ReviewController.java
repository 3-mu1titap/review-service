package com.multitap.review.review.presentation;

import com.multitap.review.common.entity.BaseResponse;
import com.multitap.review.review.application.ReviewService;
import com.multitap.review.review.dto.in.CreateReviewRequestDto;
import com.multitap.review.review.vo.in.CreateReviewRequestVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/review")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public BaseResponse<Void> createReview(
            @RequestHeader ("Uuid") String menteeUuid,
            @RequestBody CreateReviewRequestVo createReviewRequestVo) {
        log.info("Create review request: {}", createReviewRequestVo);
        reviewService.createReview(CreateReviewRequestDto.from(createReviewRequestVo, menteeUuid));
        return new BaseResponse<>();
    }
}
