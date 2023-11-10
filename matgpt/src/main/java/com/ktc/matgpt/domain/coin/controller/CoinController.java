package com.ktc.matgpt.domain.coin.controller;

import com.ktc.matgpt.domain.coin.dto.CoinRequest;
import com.ktc.matgpt.domain.coin.dto.CoinResponse;
import com.ktc.matgpt.domain.coin.service.CoinService;
import com.ktc.matgpt.security.UserPrincipal;
import com.ktc.matgpt.utils.ApiUtils;
import com.ktc.matgpt.utils.paging.PageResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@PreAuthorize("isAuthenticated()")
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
                                     @RequestBody @Valid CoinRequest.UseCoinDto useCoinDto) {

        CoinResponse.BalanceDto balanceDto = coinService.useCoin(userPrincipal.getId(), useCoinDto);
        ApiUtils.ApiSuccess<?> apiResult = ApiUtils.success(balanceDto);
        return ResponseEntity.ok(apiResult);
    }

    @PostMapping("/earn")
    public ResponseEntity<?> earnCoin(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                      @RequestBody @Valid CoinRequest.EarnCoinDto earnCoinDto) {

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
