package com.multitap.review.review.dto.in;

import com.multitap.review.review.domain.Review;
import com.multitap.review.review.vo.in.CreateReviewRequestVo;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class CreateReviewRequestDto {

    private String title;
    private String comment;
    private int score;
    private String mentoringUuid;
    private String mentoringSessionUuid;
    private String menteeUuid;
    private boolean isDeleted;

    public Review toReview(String reviewCode) {
        return Review.builder()
                .reviewCode(reviewCode)
                .title(title)
                .comment(comment)
                .score(score)
                .mentoringUuid(mentoringUuid)
                .mentoringSessionUuid(mentoringSessionUuid)
                .menteeUuid(menteeUuid)
                .isDeleted(isDeleted)
                .build();
    }

    public static CreateReviewRequestDto from(CreateReviewRequestVo createReviewRequestVo, String menteeUuid) {
        return CreateReviewRequestDto.builder()
                .title(createReviewRequestVo.getTitle())
                .comment(createReviewRequestVo.getComment())
                .score(createReviewRequestVo.getScore())
                .mentoringUuid(createReviewRequestVo.getMentoringUuid())
                .mentoringSessionUuid(createReviewRequestVo.getMentoringSessionUuid())
                .menteeUuid(menteeUuid)
                .isDeleted(false)
                .build();
    }

    @Builder
    public CreateReviewRequestDto(String title,
                                  String comment,
                                  int score,
                                  String mentoringUuid,
                                  String mentoringSessionUuid,
                                  String menteeUuid,
                                  boolean isDeleted) {
        this.title = title;
        this.comment = comment;
        this.score = score;
        this.mentoringUuid = mentoringUuid;
        this.mentoringSessionUuid = mentoringSessionUuid;
        this.menteeUuid = menteeUuid;
        this.isDeleted = false;
    }
}
