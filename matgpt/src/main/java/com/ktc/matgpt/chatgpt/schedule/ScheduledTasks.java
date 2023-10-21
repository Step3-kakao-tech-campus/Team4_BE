package com.ktc.matgpt.chatgpt.schedule;

import com.ktc.matgpt.chatgpt.annotation.Timer;
import com.ktc.matgpt.chatgpt.entity.GptReview;
import com.ktc.matgpt.chatgpt.service.GptService;
import com.ktc.matgpt.feature_review.review.ReviewService;
import com.ktc.matgpt.store.StoreResponse;
import com.ktc.matgpt.store.StoreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class ScheduledTasks {

    private static final int API_CALL_TRIGGER = 10;

    private final GptService gptService;
    private final StoreService storeService;

    /**
     * 매주 일요일 오전 4시에,
     * 모든 음식점을 순회하며 직전의 리뷰 개수보다 API_CALL_TRIGGER 이상 증가하였다면,
     * chatGpt review Summary를 갱신합니다.
     */
    @Timer
    @Transactional
    @Scheduled(cron = "0 0 4 * * SUN")
    public void callChatGPTApi() {

        List<StoreResponse.FindAllStoreDTO> stores = storeService.findAll();

        for (StoreResponse.FindAllStoreDTO store: stores) {

            int numsOfReview = store.getNumsOfReview();

            Long storeId = store.getStoreId();
            int lastNumsOfReview = gptService.getLastNumsOfReview(storeId);

            if (numsOfReview - lastNumsOfReview >= API_CALL_TRIGGER) {
                gptService.generateReviewSummary(storeId);
                log.info("[ChatGPT API] : Store-" + storeId + " Review Summary Updated!");
            }
        }
    }
}
