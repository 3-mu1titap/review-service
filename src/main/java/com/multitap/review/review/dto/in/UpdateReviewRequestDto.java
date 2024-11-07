package com.multitap.review.review.dto.in;

import com.multitap.review.review.domain.Review;
import com.multitap.review.review.vo.in.UpdateReviewRequestVo;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UpdateReviewRequestDto {

    private String reviewCode;
    private String title;
    private String comment;
    private int score;
    private String menteeUuid;

    public Review updateReview(Review review) {
        return Review.builder()
                .id(review.getId())
                .reviewCode(reviewCode)
                .title(title)
                .comment(comment)
                .score(score)
                .mentoringUuid(review.getMentoringUuid())
                .mentoringSessionUuid(review.getMentoringSessionUuid())
                .menteeUuid(menteeUuid)
                .isReported(review.isReported())
                .isConfirmed(review.isConfirmed())
                .isDeleted(review.isDeleted())
                .build();
    }

    public static UpdateReviewRequestDto from(UpdateReviewRequestVo updateReviewRequestVo, String menteeUuid) {
        return UpdateReviewRequestDto.builder()
                .reviewCode(updateReviewRequestVo.getReviewCode())
                .title(updateReviewRequestVo.getTitle())
                .comment(updateReviewRequestVo.getComment())
                .score(updateReviewRequestVo.getScore())
                .menteeUuid(menteeUuid)
                .build();
    }



}
