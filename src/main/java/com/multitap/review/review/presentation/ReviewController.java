package com.multitap.review.review.presentation;

import com.multitap.review.common.entity.BaseResponse;
import com.multitap.review.review.application.ReviewService;
import com.multitap.review.review.dto.in.CreateReviewRequestDto;
import com.multitap.review.review.dto.in.SoftDeleteReviewRequestDto;
import com.multitap.review.review.dto.in.UpdateReviewRequestDto;
import com.multitap.review.review.vo.in.CreateReviewRequestVo;
import com.multitap.review.review.vo.in.UpdateReviewRequestVo;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/review")
public class ReviewController {

    private final ReviewService reviewService;

    @Operation(summary = "리뷰 작성", description = "리뷰를 작성합니다.")
    @PostMapping
    public BaseResponse<Void> createReview(
            @RequestHeader ("userUuid") String menteeUuid,
            @RequestBody CreateReviewRequestVo createReviewRequestVo) {
        log.info("Create review request: {}", createReviewRequestVo);
        reviewService.createReview(CreateReviewRequestDto.from(createReviewRequestVo, menteeUuid), createReviewRequestVo.getMentoringName(), createReviewRequestVo.getNickName());
        return new BaseResponse<>();
    }

    @Operation(summary = "리뷰 수정", description = "리뷰를 수정합니다.")
    @PutMapping
    public BaseResponse<Void> updateReview(
            @RequestHeader ("userUuid") String menteeUuid,
            @RequestBody UpdateReviewRequestVo updateReviewRequestVo) {
        log.info("Update review request: {}", updateReviewRequestVo);
        reviewService.updateReview(UpdateReviewRequestDto.from(updateReviewRequestVo, menteeUuid));
        return new BaseResponse<>();
    }

    @Operation(summary = "리뷰 삭제", description = "리뷰를 삭제합니다.(softDelete)")
    @PutMapping("/softDelete/{reviewCode}")
    public BaseResponse<Void> softDeleteReview(
            @RequestHeader ("userUuid") String menteeUuid,
            @PathVariable String reviewCode) {
        log.info("Soft delete review request: {}", reviewCode);
        reviewService.softDeleteReview(SoftDeleteReviewRequestDto.of(reviewCode, menteeUuid));
        return new BaseResponse<>();
    }

}
