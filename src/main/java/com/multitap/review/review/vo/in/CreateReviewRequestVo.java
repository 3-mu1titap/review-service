package com.multitap.review.review.vo.in;

import lombok.Getter;
import lombok.ToString;
import org.bouncycastle.crypto.agreement.srp.SRP6Client;

@Getter
@ToString
public class CreateReviewRequestVo {

    private String title;
    private String comment;
    private int score;
    private String mentoringUuid;
    private String mentoringSessionUuid;
    private String mentorUuid;
}
