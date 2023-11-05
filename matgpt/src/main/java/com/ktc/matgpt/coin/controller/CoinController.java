package com.ktc.matgpt.coin.controller;

import com.ktc.matgpt.coin.dto.CoinRequest;
import com.ktc.matgpt.coin.dto.CoinResponse;
import com.ktc.matgpt.coin.service.CoinService;
import com.ktc.matgpt.security.UserPrincipal;
import com.ktc.matgpt.utils.ApiUtils;
import com.ktc.matgpt.utils.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@RequestMapping("/coin")
@RestController
public class CoinController {

    private final CoinService coinService;

    @GetMapping("")
    public ResponseEntity<?> getCoinBalance(@AuthenticationPrincipal UserPrincipal userPrincipal) {

        CoinResponse.BalanceDto balanceDto = coinService.getCoinBalance(userPrincipal.getId());
        ApiUtils.ApiSuccess<?> apiResult = ApiUtils.success(balanceDto);
        return ResponseEntity.ok(apiResult);
    }

    @PostMapping("/use")
    public ResponseEntity<?> useCoin(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                     @RequestBody CoinRequest.UseCoinDto useCoinDto) {

        CoinResponse.BalanceDto balanceDto = coinService.useCoin(userPrincipal.getId(), useCoinDto);
        ApiUtils.ApiSuccess<?> apiResult = ApiUtils.success(balanceDto);
        return ResponseEntity.ok(apiResult);
    }

    @PostMapping("/earn")
    public ResponseEntity<?> earnCoin(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                      @RequestBody CoinRequest.EarnCoinDto earnCoinDto) {

        CoinResponse.BalanceDto balanceDto = coinService.earnCoin(userPrincipal.getId(), earnCoinDto);
        ApiUtils.ApiSuccess<?> apiResult = ApiUtils.success(balanceDto);
        return ResponseEntity.ok(apiResult);
    }

    @GetMapping("/history/usage")
    public ResponseEntity<?> getCoinUsageHistory(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                 @RequestParam(required = false) LocalDateTime cursor,
                                                 @RequestParam(required = false, value = "cursorid") Long cursorId) {

        PageResponse<?, CoinResponse.UsageHistoryDto> usageHistoriesDto = coinService.getCoinUsageHistory(userPrincipal.getId(), cursor, cursorId);
        ApiUtils.ApiSuccess<?> apiResult = ApiUtils.success(usageHistoriesDto);
        return ResponseEntity.ok(apiResult);
    }

    @GetMapping("/history/earning")
    public ResponseEntity<?> getCoinEarningHistory(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                   @RequestParam(required = false) LocalDateTime cursor,
                                                   @RequestParam(required = false, value = "cursorid") Long cursorId) {

        PageResponse<?, CoinResponse.EarningHistoryDto> earningHistoriesDto = coinService.getCoinEarningHistory(userPrincipal.getId(), cursor, cursorId);
        ApiUtils.ApiSuccess<?> apiResult = ApiUtils.success(earningHistoriesDto);
        return ResponseEntity.ok(apiResult);
    }
}
