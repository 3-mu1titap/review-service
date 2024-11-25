package com.multitap.review.review.dto.in;

import com.multitap.review.review.domain.Review;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SoftDeleteReviewRequestDto {

    private String reviewCode;
    private String menteeUuid;

    public Review SoftDeleteReview(Review review) {
        return Review.builder()
                .id(review.getId())
                .reviewCode(reviewCode)
                .title(review.getTitle())
                .comment(review.getComment())
                .score(review.getScore())
                .mentoringUuid(review.getMentoringUuid())
                .mentoringSessionUuid(review.getMentoringSessionUuid())
                .menteeUuid(menteeUuid)
                .isDeleted(true)
                .build();
    }

    public static SoftDeleteReviewRequestDto of(String reviewCode, String menteeUuid) {
        return SoftDeleteReviewRequestDto.builder()
                .reviewCode(reviewCode)
                .menteeUuid(menteeUuid)
                .build();
    }
}
