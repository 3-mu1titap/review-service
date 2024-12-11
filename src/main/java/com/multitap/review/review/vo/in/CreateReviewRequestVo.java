package com.multitap.review.review.vo.in;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class CreateReviewRequestVo {

    private String title;
    private String comment;
    private int score;
    private String mentoringUuid;
    private String mentoringSessionUuid;
    private String mentorUuid;
    private String menteeUuid;
    private String mentoringName;
    private String nickName;
}
