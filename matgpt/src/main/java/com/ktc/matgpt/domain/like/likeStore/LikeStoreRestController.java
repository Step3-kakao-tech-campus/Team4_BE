package com.ktc.matgpt.domain.like.likeStore;

import com.ktc.matgpt.domain.like.usecase.CreateLikeStoreUseCase;
import com.ktc.matgpt.security.UserPrincipal;
import com.ktc.matgpt.utils.ApiUtils;
import com.ktc.matgpt.utils.paging.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "", produces = {"application/json; charset=UTF-8"})
public class LikeStoreRestController {

    private final LikeStoreService likeStoreService;
    private final CreateLikeStoreUseCase createLikeStoreUsecase;

    //특정 Store 즐겨찾기 추가하기
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/stores/{storeId}/like")
    public ResponseEntity<?> toggleHeart(@PathVariable Long storeId, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        boolean isHeartAdded = createLikeStoreUsecase.execute(storeId, userPrincipal.getEmail());

        String message = isHeartAdded ? "즐겨찾기 성공" : "즐겨찾기 취소 성공";
        ApiUtils.ApiSuccess<?> apiResult = ApiUtils.success(message);

        return ResponseEntity.ok(apiResult);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/stores/like")
    public ResponseEntity<?> findAllStores(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                           @RequestParam(required = false) Long cursorId) {
        PageResponse<Long, LikeStoreResponseDTO.FindAllLikeStoresDTO> heartResponseDTO
                                            = likeStoreService.findLikeStoresByUserEmail(userPrincipal.getEmail(), cursorId);
        return ResponseEntity.ok(ApiUtils.success(heartResponseDTO));

    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/stores/{storeId}/if-liked")
    public ResponseEntity<?> checkIfLikedAlready(@PathVariable Long storeId, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        boolean hasLikedAlready = createLikeStoreUsecase.isHeartAlreadyExists(userPrincipal.getEmail(), storeId);

        return ResponseEntity.ok(ApiUtils.success(new LikeStoreResponseDTO.HasLikedDTO(hasLikedAlready)));
    }
}
