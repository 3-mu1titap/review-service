package com.multitap.review.review.infrastructure;

import com.multitap.review.review.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    boolean existsByMenteeUuidAndMentoringSessionUuid(String menteeUuid, String mentoringSessionUuid);
    Optional<Review> findByReviewCodeAndMenteeUuid(String reviewCode, String menteeUuid);
}
