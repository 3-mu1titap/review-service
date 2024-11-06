package com.multitap.review.review.infrastructure;

import com.multitap.review.review.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {

}
