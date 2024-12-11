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
@Transactional
public class KafkaConsumer {

    private final ReviewRepository reviewRepository;
    private final KafkaProducerService kafkaProducerService;

    private boolean mentoringDataReceived = false;
    private boolean memberDataReceived = false;

    // 여기에 제목 리스트를 추가하시면 됩니다
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

    // 여기에 내용 리스트를 추가하시면 됩니다
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

    @KafkaListener(topics = "mentoring-data", containerFactory = "mentoringDtoListener")
    public void processMentoringData(MentoringDataDto mentoringDataDto) {
        reviewDataDto.setMentoringUuid(mentoringDataDto.getMentoringUuid());
        reviewDataDto.setMentoringSessionUuid(mentoringDataDto.getSessionUuid());
        mentoringDataReceived = true;

        checkAndProcess();
    }

    @KafkaListener(topics = "member-data", containerFactory = "memberUuidDtoListener")
    public void processMemberUuidData(MemberUuidDataDto memberUuidDataDto) {
        reviewDataDto.setMenteeUuid(memberUuidDataDto.getMenteeUuid());
        reviewDataDto.setMentorUuid(memberUuidDataDto.getMentorUuid());
        memberDataReceived = true;

        checkAndProcess();
    }

    private void checkAndProcess() {
        if (mentoringDataReceived && memberDataReceived) {
            buildReviewDataDto();
            createAndSaveReviews();
            mentoringDataReceived = false;
            memberDataReceived = false;
        }
    }

    private void buildReviewDataDto() {
        log.info("ReviewDataDto prepared: {}", reviewDataDto);
    }

    private void createAndSaveReviews() {
        Random random = new Random();

        for (int i = 0; i < reviewDataDto.getMentoringUuid().size(); i++) {
            int score = random.nextInt(3) + 3;
            String title = titles.get(random.nextInt(titles.size()));
            String comment = comments.get(random.nextInt(comments.size()));

            Review review = Review.builder()
                    .reviewCode(ReviewUuidGenerator.generateUniqueReviewCode("RV-"))
                    .title(title)
                    .comment(comment)
                    .score(score)
                    .mentoringUuid(reviewDataDto.getMentoringUuid().get(i))
                    .mentoringSessionUuid(reviewDataDto.getMentoringSessionUuid().get(i))
                    .menteeUuid(reviewDataDto.getMenteeUuid().get(i))
                    .mentorUuid(reviewDataDto.getMentorUuid().get(i))
                    .isDeleted(false)
                    .build();

            log.info("Review saved: {}", review);

            ReviewDto reviewDto = ReviewDto.from(reviewRepository.save(review));
            kafkaProducerService.sendReview(reviewDto);
        }

        reviewDataDto = new ReviewDataDto();
    }
}