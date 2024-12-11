package com.multitap.review.review.kafka.consumer;

import com.multitap.review.common.utils.ReviewUuidGenerator;
import com.multitap.review.review.domain.Review;
import com.multitap.review.review.infrastructure.ReviewRepository;
import com.multitap.review.review.kafka.ReviewDataDto;
import com.multitap.review.review.kafka.consumer.messagein.MemberUuidDataDto;
import com.multitap.review.review.kafka.consumer.messagein.MentoringDataDto;
import com.multitap.review.review.kafka.producer.KafkaProducerService;
import com.multitap.review.review.kafka.producer.ReviewDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaConsumer {

    private final ReviewRepository reviewRepository;
    private final KafkaProducerService kafkaProducerService;

    private final List<String> titles = List.of(
            "멘토링 후기입니다",
            "유익했던 멘토링",
            "추천하고 싶은 멘토",
            "성장할 수 있었던 시간",
            "멘토링 이용 후기",
            "진로 상담 후기",
            "2차 멘토링 후기",
            "취업 멘토링 후기",
            "커리어 상담 후기",
            "감동적인 멘토링"
    );

    private final List<String> comments = List.of(
            "멘토님의 전문적인 조언 덕분에 많은 도움이 되었습니다. 특히 실무 경험을 바탕으로 한 조언들이 매우 유익했어요.",
            "처음에는 망설였는데 멘토링을 받고나서 진로에 대한 확신이 생겼습니다. 구체적인 방향을 제시해주셔서 감사합니다.",
            "질문에 대해 상세하게 답변해주시고, 제가 미처 생각하지 못한 부분까지 짚어주셔서 큰 도움이 되었습니다.",
            "실제 업무 환경에서 필요한 스킬과 지식을 자세히 설명해주셔서 매우 만족스러웠습니다.",
            "멘토님의 피드백을 통해 부족한 부분을 파악하고 개선할 수 있었습니다. 정말 감사드립니다!",
            "취준생 입장에서 필요한 기술과 준비사항을 잘 설명해주셨습니다. 앞으로의 공부 방향을 잡는데 큰 도움이 되었어요.",
            "실무에서 자주 접하는 문제들과 해결 방법에 대해 자세히 설명해주셔서 실질적인 도움이 되었습니다.",
            "멘토링을 통해 제가 놓치고 있던 부분들을 꼼꼼히 짚어주셔서 많이 배웠습니다.",
            "기술 면접 준비에 대한 조언이 특히 유익했습니다. 예상 질문들과 답변 방향을 잘 알려주셨어요.",
            "프로젝트 경험에 대한 조언과 포트폴리오 작성 팁을 상세히 알려주셔서 취업 준비에 큰 도움이 되었습니다."
    );

    private ReviewDataDto reviewDataDto = new ReviewDataDto();
    private boolean mentoringDataReceived = false;
    private boolean memberDataReceived = false;

    @KafkaListener(topics = "mentoring-data", containerFactory = "mentoringDtoListener")
    public void processMentoringData(MentoringDataDto mentoringDataDto) {
        log.info("Received mentoring data: {}", mentoringDataDto);
        reviewDataDto.setMentoringUuid(mentoringDataDto.getMentoringUuid());
        reviewDataDto.setMentoringSessionUuid(mentoringDataDto.getSessionUuid());
        mentoringDataReceived = true;
        checkAndProcess();
    }

    @KafkaListener(topics = "member-data", containerFactory = "memberUuidDtoListener")
    public void processMemberUuidData(MemberUuidDataDto memberUuidDataDto) {
        log.info("Received member data: {}", memberUuidDataDto);
        reviewDataDto.setMenteeUuid(memberUuidDataDto.getMenteeUuid());
        reviewDataDto.setMentorUuid(memberUuidDataDto.getMentorUuid());
        memberDataReceived = true;
        checkAndProcess();
    }

    private void checkAndProcess() {
        if (mentoringDataReceived && memberDataReceived) {
            createAndSaveReviews();
            resetState();
        }
    }

    private void createAndSaveReviews() {
        Random random = new Random();
        int mentoringSize = reviewDataDto.getMentoringUuid().size();
        int menteeSize = reviewDataDto.getMenteeUuid().size();
        int mentorSize = reviewDataDto.getMentorUuid().size();

        for (int i = 0; i < mentoringSize; i++) {
            // 리스트 길이에 관계없이 반복적으로 값을 가져올 수 있도록 인덱스 계산
            int menteeIndex = i % menteeSize;
            int mentorIndex = i % mentorSize;

            Review review = Review.builder()
                    .reviewCode(ReviewUuidGenerator.generateUniqueReviewCode("RV-"))
                    .title(titles.get(random.nextInt(titles.size())))
                    .comment(comments.get(random.nextInt(comments.size())))
                    .score(random.nextInt(3) + 3) // 3-5점 사이의 랜덤 점수
                    .mentoringUuid(reviewDataDto.getMentoringUuid().get(i))
                    .mentoringSessionUuid(reviewDataDto.getMentoringSessionUuid().get(i))
                    .menteeUuid(reviewDataDto.getMenteeUuid().get(menteeIndex))  // 순환되는 인덱스 사용
                    .mentorUuid(reviewDataDto.getMentorUuid().get(mentorIndex))  // 순환되는 인덱스 사용
                    .isDeleted(false)
                    .build();

            Review savedReview = reviewRepository.save(review);
            log.info("Review saved: {}", savedReview);

            ReviewDto reviewDto = ReviewDto.from(savedReview);
            kafkaProducerService.sendReview(reviewDto);
        }
    }

    private void resetState() {
        reviewDataDto = new ReviewDataDto();
        mentoringDataReceived = false;
        memberDataReceived = false;
    }
}