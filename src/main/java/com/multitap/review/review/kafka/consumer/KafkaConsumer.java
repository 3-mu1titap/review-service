package com.multitap.review.review.kafka.consumer;

import com.multitap.review.common.utils.ReviewUuidGenerator;
import com.multitap.review.review.domain.Review;
import com.multitap.review.review.infrastructure.ReviewRepository;
import com.multitap.review.review.kafka.consumer.messagein.MemberUuidDataDto;
import com.multitap.review.review.kafka.producer.KafkaProducerService;
import com.multitap.review.review.kafka.producer.ReviewDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaConsumer {

    private final ReviewRepository reviewRepository;
    private final KafkaProducerService kafkaProducerService;

    // 고정된 UUID 세트
    private final List<String> bestMentorUuids = List.of(
            "d9fb8103-9591-4b6e-a1ca-9f1cd07452aa",
            "89e64528-fe69-4ec1-ae37-1747352800e6",
            "a0a5ef1b-a6fc-4ed7-b861-036d7b8667f8",
            "6f701734-091c-446c-907a-0e2dbe405907",
            "361e480b-cf24-4b4b-a812-908cc54618c1",
            "86d56195-d2fc-417b-b588-aaf9e8ccebf9",
            "e02eba8f-605d-4112-b8b6-9af962f14dd0",
            "a95fe1d9-db6d-4d17-990c-9920e389e707",
            "dcd5a8a6-6e08-42bd-be88-1dc6440a524a",
            "b75c2110-e9ea-479d-be40-5a6d459c2bbe"
    );

    private final List<String> targetMentoringUuids = List.of(
            "a8af5a1c-01b5-4e58-95e0-ca985e3fdf99",
            "7853a101-4706-46aa-9497-7b07b2b47a94",
            "c938259e-be43-451a-be17-6f1125fa4448",
            "adc5de0f-1bd8-4ea6-aaa7-8c7170808980",
            "a3b4fa61-c605-4b83-9396-f7349ec73b58",
            "78bdba48-0207-44a0-b1ed-3066e98e080f",
            "fdf14023-3c07-40ea-b958-56a595716a52",
            "e6e80a27-2c9c-48db-bac1-e404fb4c1d60",
            "0cba11d9-1914-4254-a3e3-6695b1841b69",
            "dae71cfc-9927-4235-a715-0f9356505b5b"
    );

    private final List<String> targetSessionUuids = List.of(
            "b539d3ce-e152-4724-a7c5-527ba51fd236",
            "4354d939-3714-4381-9c2a-0c61a1e966a3",
            "c484f3ce-1723-48dc-baf4-99fc6c95a19f",
            "8c94a1a3-c946-4652-9d5c-82a63f8794fd",
            "1c2ec257-0676-42db-a83b-a1df567cabb7",
            "3cb40258-910d-4232-ab46-49363a857b84",
            "b97a6ca8-be80-487e-b331-93f1da8cf499",
            "373e0482-42a1-4e8e-bded-c81dbc6e57ba",
            "643b01bb-36d7-4f6c-b00f-b7c011e6feda",
            "d479cf77-dfc5-4206-9ab7-5cd731bc11cd"
    );

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
            "감동적인 멘토링",
            "전문적인 멘토링 후기",
            "실무 경험 공유 후기",
            "성장의 기회였던 멘토링",
            "맞춤형 커리어 상담",
            "최고의 멘토링"
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

    @KafkaListener(topics = "member-data", containerFactory = "memberUuidDtoListener")
    public void processMemberUuidData(MemberUuidDataDto memberUuidDataDto) {
        log.info("Received member data: {}", memberUuidDataDto);

        List<String> menteeUuids = memberUuidDataDto.getMenteeUuid();

        // 리뷰 생성
        createAndSaveReviews(menteeUuids);
    }

    private void createAndSaveReviews(List<String> menteeUuids) {
        Random random = new Random();

        for (int i = 0; i < bestMentorUuids.size(); i++) {
            String mentorUuid = bestMentorUuids.get(i);
            String mentoringUuid = targetMentoringUuids.get(i);
            String sessionUuid = targetSessionUuids.get(i);

            // 각 멘토별로 300~400개의 랜덤한 리뷰 생성
            int reviewCount = random.nextInt(101) + 300; // 300 to 400 reviews

            for (int j = 0; j < reviewCount; j++) {
                String menteeUuid = menteeUuids.get(random.nextInt(menteeUuids.size()));

                Review review = Review.builder()
                        .reviewCode(ReviewUuidGenerator.generateUniqueReviewCode("RV-"))
                        .title(titles.get(random.nextInt(titles.size())))
                        .comment(generateDynamicComment(random))
                        .score(generateWeightedScore(random))
                        .mentoringUuid(mentoringUuid)
                        .mentoringSessionUuid(sessionUuid)
                        .menteeUuid(menteeUuid)
                        .mentorUuid(mentorUuid)
                        .isDeleted(false)
                        .build();

                Review savedReview = reviewRepository.save(review);
                log.info("Saved review for mentor: {} | Review: {}", mentorUuid, savedReview);

                kafkaProducerService.sendReview(ReviewDto.from(savedReview));
            }
        }
    }

    private String generateDynamicComment(Random random) {
        if (random.nextDouble() < 0.3) {
            return comments.get(random.nextInt(comments.size()));
        }

        List<String> prefixes = List.of(
                "정말 훌륭한 멘토링이었습니다! ",
                "최고의 멘토링 경험이었습니다. ",
                "다시 한 번 찾고 싶은 멘토님입니다. ",
                "전문성이 돋보이는 멘토링이었습니다. ",
                "적극 추천하고 싶은 멘토님입니다. "
        );

        String baseComment = comments.get(random.nextInt(comments.size()));
        return prefixes.get(random.nextInt(prefixes.size())) + baseComment;
    }

    private int generateWeightedScore(Random random) {
        double rand = random.nextDouble();
        if (rand < 0.7) return 5;
        else if (rand < 0.9) return 4;
        else return 3;
    }
}
