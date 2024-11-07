package com.multitap.review.review.vo.in;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class UpdateReviewRequestVo {

    private String reviewCode;
    private String title;
    private String comment;
    private int score;

}
