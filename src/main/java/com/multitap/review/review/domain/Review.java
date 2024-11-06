package com.multitap.review.review.domain;

import com.multitap.review.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Comment;

@Getter
@ToString
@Entity
@NoArgsConstructor
public class Review extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Comment("리뷰 코드")
    @Column(nullable = false)
    private String reviewCode;

    @Comment("리뷰 제목")
    @Column(nullable = false)
    private String title;

    @Comment("리뷰 내용")
    @Column(nullable = false)
    private String comment;

    @Comment("별점")
    @Column(nullable = false)
    private int score;

    @Comment("멘토링 uuid")
    @Column(nullable = false)
    private String mentoringUuid;

    @Comment("멘티 uuid")
    @Column(nullable = false)
    private String menteeUuid;

    @Comment("신고 여부")
    @Column(nullable = false)
    private boolean isReported;

    @Comment("신고 조치 여부")
    @Column(nullable = false)
    private boolean isConfirmed;

    @Comment("삭제 여부")
    @Column(nullable = false)
    private boolean idDeleted;
}
