package com.multitap.review.review.application;

import com.multitap.review.review.dto.in.CreateReviewRequestDto;
import com.multitap.review.review.dto.in.UpdateReviewRequestDto;

public interface ReviewService {

    void createReview(CreateReviewRequestDto createReviewRequestDto);
    void updateReview(UpdateReviewRequestDto updateReviewRequestDto);
}
