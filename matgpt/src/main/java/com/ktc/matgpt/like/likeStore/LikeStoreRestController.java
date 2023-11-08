package com.ktc.matgpt.like.likeStore;


import com.ktc.matgpt.like.usecase.CreateLikeStoreUseCase;
import com.ktc.matgpt.security.UserPrincipal;
import com.ktc.matgpt.utils.ApiUtils;
import com.ktc.matgpt.utils.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;



@RestController
@RequiredArgsConstructor
@RequestMapping(value = "", produces = {"application/json; charset=UTF-8"})
public class LikeStoreRestController {

    private final LikeStoreService likeStoreService;
    private final CreateLikeStoreUseCase createLikeStoreUsecase;

    //특정 Store 즐겨찾기 추가하기
    @PostMapping("/stores/{storeId}/like")
    public ResponseEntity<?> toggleHeart(@PathVariable Long storeId, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        boolean isHeartAdded = createLikeStoreUsecase.execute(storeId, userPrincipal.getEmail());

        String message = isHeartAdded ? "즐겨찾기 성공" : "즐겨찾기 취소 성공";
        ApiUtils.ApiSuccess<?> apiResult = ApiUtils.success(message);

        return ResponseEntity.ok(apiResult);
    }

    @GetMapping("/stores/like")
    public ResponseEntity<?> findAllStores(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                           @RequestParam(required = false) Long cursorId) {
        PageResponse<Long, LikeStoreResponseDTO.FindAllLikeStoresDTO> heartResponseDTO
                                            = likeStoreService.findLikeStoresByUserEmail(userPrincipal.getEmail(), cursorId);
        return ResponseEntity.ok(ApiUtils.success(heartResponseDTO));

    }



    //특정 store에 좋아요 취소하기
//    @DeleteMapping("/stores/{storeId}/like")
//    public ResponseEntity<?> deleteHeart(@PathVariable(value = "storeId", required = true) Long storeId , @AuthenticationPrincipal UserPrincipal userPrincipal){
//
//    }
}
