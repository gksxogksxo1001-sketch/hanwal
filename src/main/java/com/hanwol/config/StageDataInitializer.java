package com.hanwol.config;

import com.hanwol.domain.story.Stage;
import com.hanwol.domain.story.StageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class StageDataInitializer implements CommandLineRunner {

    private final StageRepository stageRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        // 이미 스테이지가 많이 생성되어 있다면 패스!
        if (stageRepository.count() > 150) {
            log.info("[StageInitializer] 이미 프롤로그 및 10막 분량의 데이터가 존재하여 자동 생성을 패스합니다.");
            return;
        }

        log.info("[StageInitializer] 프롤로그 및 1막~10막 무림 서사 대확장 빌드를 시작합니다.");

        // 0. 프롤로그 (0막) 생성 (1~5 스테이지)
        for (int stageNum = 1; stageNum <= 5; stageNum++) {
            Stage stage = Stage.builder()
                    .id((long) stageNum)
                    .chapterId(0L)
                    .stageNum(stageNum)
                    .title("프롤로그 " + stageNum + "장: 강호의 서막")
                    .monsterGroupId((long) stageNum)
                    .rewardGold(100 + (stageNum * 20))
                    .rewardExp(50 + (stageNum * 10))
                    .build();
            stageRepository.save(stage);
        }

        // 1. 1막~10막 생성 (각 15스테이지)
        for (int chapter = 1; chapter <= 10; chapter++) {
            String actTheme = "";
            if (chapter <= 3) actTheme = "시스템 학습 및 동료 확장";
            else if (chapter <= 7) actTheme = "본격적인 기믹 및 파밍";
            else actTheme = "엔드 콘텐츠 및 전초전";

            for (int stageNum = 1; stageNum <= 15; stageNum++) {
                long stageId = (chapter * 100) + stageNum;
                int rewardGold = (chapter * 300) + (stageNum * 50);
                int rewardExp = (chapter * 150) + (stageNum * 20);

                Stage stage = Stage.builder()
                        .id(stageId)
                        .chapterId((long) chapter)
                        .stageNum(stageNum)
                        .title(chapter + "막 " + stageNum + "장: [" + actTheme + "]")
                        .monsterGroupId(stageId)
                        .rewardGold(rewardGold)
                        .rewardExp(rewardExp)
                        .build();

                stageRepository.save(stage);
            }
        }
        log.info("[StageInitializer] 프롤로그와 10막 총 155개의 대서사 스테이지 빌드가 완료되었습니다!");
    }
}