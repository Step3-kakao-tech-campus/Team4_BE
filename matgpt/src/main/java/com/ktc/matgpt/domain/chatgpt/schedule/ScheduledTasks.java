package com.ktc.matgpt.domain.chatgpt.schedule;

import com.ktc.matgpt.domain.chatgpt.annotation.Timer;
import com.ktc.matgpt.domain.chatgpt.dto.GptApiResponse;
import com.ktc.matgpt.domain.chatgpt.dto.GptReviewSummaryResponse;
import com.ktc.matgpt.domain.chatgpt.service.GptReviewSummaryService;
import com.ktc.matgpt.domain.chatgpt.utils.UnixTimeConverter;
import com.ktc.matgpt.domain.store.StoreResponse;
import com.ktc.matgpt.domain.store.StoreService;
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

    private final GptReviewSummaryService gptReviewSummaryService;
    private final StoreService storeService;

    /**
     * 매주 일요일 오전 4시에,
     * 모든 음식점을 순회하며 직전의 리뷰 개수보다 API_CALL_TRIGGER 이상 증가하였다면,
     * chatGpt review Summary를 갱신합니다.
     */
    @Timer
    @Scheduled(cron = "0 0 4 * * SUN")
    public void getReviewSummarysFromChatGptApi() {

        List<GptReviewSummaryResponse> gptReviewSummaryResponse = storeService.findAllForGpt().stream()
                .filter(store -> getReviewDifference(store) >= API_CALL_TRIGGER)
                .map(store -> gptReviewSummaryService.generateReviewSummarys(store.getStoreId()))
                .flatMap(List::stream)
                .toList();

        processGptResponses(gptReviewSummaryResponse);
    }

    private void processGptResponses(List<GptReviewSummaryResponse> gptReviewSummaryResponses) {
        gptReviewSummaryResponses.forEach(gptReviewSummaryResponse -> {
            GptApiResponse gptApiResponse = gptReviewSummaryResponse.gptApiResponse().join();

            if (gptApiResponse == null) {
                log.error("[ChatGPT API] : storeId-{} GptApiResponse : null", gptReviewSummaryResponse.storeId());
                return;
            }

            LocalDateTime createdAt = UnixTimeConverter.toLocalDateTime(gptApiResponse.created());
            String reviewSummary = gptApiResponse.getContent();

            if (reviewSummary == null) {
                log.error("[ChatGPT API] : storeId-{} 리뷰 요약을 생성하는데 실패했습니다.", gptReviewSummaryResponse.storeId());
                return;
            }

            gptReviewSummaryService.updateOrCreateGptReview(gptReviewSummaryResponse.storeId(), gptReviewSummaryResponse.summaryType(), reviewSummary, createdAt);
        });
    }

    private int getReviewDifference(StoreResponse.FindAllDTO store) {
        return store.getNumsOfReview() - gptReviewSummaryService.getLastNumsOfReview(store.getStoreId());
    }
}
