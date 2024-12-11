package com.multitap.review.review.application;

import com.multitap.review.review.dto.in.CreateReviewRequestDto;
import com.multitap.review.review.dto.in.SoftDeleteReviewRequestDto;
import com.multitap.review.review.dto.in.UpdateReviewRequestDto;

public interface ReviewService {

    void createReview(CreateReviewRequestDto createReviewRequestDto, String mentoringName, String nickName);
    void updateReview(UpdateReviewRequestDto updateReviewRequestDto);
    void softDeleteReview(SoftDeleteReviewRequestDto softDeleteReviewRequestDto);
}
