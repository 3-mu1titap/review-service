package com.multitap.review.common.utils;

import java.util.UUID;

public class ReviewUuidGenerator {

    public static String generateReviewUuid(String prefix) {
        return prefix + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
