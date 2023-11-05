package com.ktc.matgpt.chatgpt.schedule;

import com.ktc.matgpt.chatgpt.annotation.Timer;
import com.ktc.matgpt.chatgpt.dto.GptApiResponse;
import com.ktc.matgpt.chatgpt.dto.GptResponse;
import com.ktc.matgpt.chatgpt.service.GptService;
import com.ktc.matgpt.chatgpt.utils.UnixTimeConverter;
import com.ktc.matgpt.store.StoreResponse;
import com.ktc.matgpt.store.StoreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
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
    @Scheduled(cron = "0 0 4 * * SUN")
    public void getReviewSummarysFromChatGptApi() {

        List<GptResponse> gptResponses = storeService.findAllForGpt().stream()
                .filter(store -> getReviewDifference(store) >= API_CALL_TRIGGER)
                .map(store -> gptService.generateReviewSummarys(store.getStoreId()))
                .flatMap(List::stream)
                .toList();

        processGptResponses(gptResponses);
    }

    private void processGptResponses(List<GptResponse> gptResponses) {
        gptResponses.forEach(gptResponse -> {
            GptApiResponse gptApiResponse = gptResponse.gptApiResponse().join();

            if (gptApiResponse == null) {
                log.error("[ChatGPT API] : storeId-{} GptApiResponse : null", gptResponse.storeId());
                return;
            }

            String reviewSummary = gptApiResponse.getContent();
            LocalDateTime createdAt = UnixTimeConverter.toLocalDateTime(gptApiResponse.created());

            if (reviewSummary == null) {
                log.error("[ChatGPT API] : storeId-{} 리뷰 요약을 생성하는데 실패했습니다.", gptResponse.storeId());
                return;
            }

            gptService.updateOrCreateGptReview(gptResponse.storeId(), gptResponse.summaryType(), reviewSummary, createdAt);
        });
    }

    private int getReviewDifference(StoreResponse.FindAllDTO store) {
        return store.getNumsOfReview() - gptService.getLastNumsOfReview(store.getStoreId());
    }
}
