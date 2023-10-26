package com.ktc.matgpt.chatgpt.schedule;

import com.ktc.matgpt.chatgpt.service.GptService;
import com.ktc.matgpt.store.StoreResponse;
import com.ktc.matgpt.store.StoreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ExecutionException;

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
    @Scheduled(cron = "0 0 4 * * SUN")
    public void callChatGPTApi() throws ExecutionException, InterruptedException {

        List<StoreResponse.FindAllDTO> stores = storeService.findAll();
        List<StoreResponse.FindAllDTO> targetStores = stores.stream()
                .filter(store -> getReviewDifference(store) >= API_CALL_TRIGGER)
                .toList();

        for (StoreResponse.FindAllDTO store : targetStores) {
            Long storeId = store.getStoreId();
            gptService.generateReviewSummarys(storeId);
        }
    }

    private int getReviewDifference(StoreResponse.FindAllDTO store) {
        return store.getNumsOfReview() - gptService.getLastNumsOfReview(store.getStoreId());
    }
}
