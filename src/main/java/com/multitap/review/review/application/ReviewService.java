package com.multitap.review.review.application;

import com.multitap.review.review.dto.in.CreateReviewRequestDto;

public interface ReviewService {

    void createReview(CreateReviewRequestDto createReviewDto);
}
