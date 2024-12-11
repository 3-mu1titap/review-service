package com.multitap.review.review.kafka.producer;

import com.multitap.review.review.domain.Review;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@ToString
@Getter
@NoArgsConstructor
public class ReviewDto {

    private String reviewCode;
    private String reviewTitle;
    private String reviewComment;
    private String menteeUuid;
    private String mentorUuid;
    private String mentoringUuid;
    private String mentoringSessionUuid;
    private int score;
    private boolean isDeleted;
    private LocalDateTime wroteAt;
    private String mentoringName;
    private String nickName;

    @Builder
    public ReviewDto(String reviewCode, String reviewTitle, String reviewComment, String menteeUuid, String mentorUuid, String mentoringUuid, String mentoringSessionUuid, int score, boolean isDeleted, LocalDateTime wroteAt, String mentoringName, String nickName) {
        this.reviewCode = reviewCode;
        this.reviewTitle = reviewTitle;
        this.reviewComment = reviewComment;
        this.menteeUuid = menteeUuid;
        this.mentorUuid = mentorUuid;
        this.mentoringUuid = mentoringUuid;
        this.mentoringSessionUuid = mentoringSessionUuid;
        this.score = score;
        this.isDeleted = isDeleted;
        this.wroteAt = wroteAt;
        this.mentoringName = mentoringName;
        this.nickName = nickName;
    }

    public static ReviewDto createFrom(Review review, String mentoringName, String nickName) {
        return ReviewDto.builder()
                .reviewCode(review.getReviewCode())
                .reviewTitle(review.getTitle())
                .reviewComment(review.getComment())
                .menteeUuid(review.getMenteeUuid())
                .mentorUuid(review.getMentorUuid())
                .mentoringUuid(review.getMentoringUuid())
                .mentoringSessionUuid(review.getMentoringSessionUuid())
                .score(review.getScore())
                .isDeleted(review.isDeleted())
                .wroteAt(review.getCreatedAt())
                .mentoringName(mentoringName)
                .nickName(nickName)
                .build();
    }

    public static ReviewDto from(Review review) {
        return ReviewDto.builder()
                .reviewCode(review.getReviewCode())
                .reviewTitle(review.getTitle())
                .reviewComment(review.getComment())
                .menteeUuid(review.getMenteeUuid())
                .mentorUuid(review.getMentorUuid())
                .mentoringUuid(review.getMentoringUuid())
                .mentoringSessionUuid(review.getMentoringSessionUuid())
                .score(review.getScore())
                .isDeleted(review.isDeleted())
                .wroteAt(review.getCreatedAt())
                .build();
    }
}
