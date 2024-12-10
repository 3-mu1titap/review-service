package com.multitap.review.review.kafka.consumer.messagein;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class MentoringDataDto {

    List<String> mentoringUuid;
    List<String> sessionUuid;

    @Builder
    public MentoringDataDto(List<String> mentoringUuid, List<String> sessionUuid) {
        this.mentoringUuid = mentoringUuid;
        this.sessionUuid = sessionUuid;
    }
}