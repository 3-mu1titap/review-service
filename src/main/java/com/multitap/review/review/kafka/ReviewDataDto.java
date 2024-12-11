package com.multitap.review.review.kafka;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDataDto {

    private List<String> mentoringUuid;
    private List<String> mentoringSessionUuid;
    private List<String> menteeUuid;
    private List<String> mentorUuid;

}
