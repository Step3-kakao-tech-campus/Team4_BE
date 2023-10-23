package com.ktc.matgpt.like.likeStore;


import com.ktc.matgpt.security.UserPrincipal;
import com.ktc.matgpt.utils.ApiUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;



@RestController
@RequiredArgsConstructor
@RequestMapping("")
public class LikeStoreRestController {

    private final LikeStoreService likeStoreService;

    //특정 Store 즐겨찾기 추가하기
    @PostMapping("/stores/{storeId}/like")
    public ResponseEntity<?> toggleHeart(@PathVariable Long storeId, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        boolean isHeartAdded = likeStoreService.toggleHeartForStore(storeId, userPrincipal.getEmail());

        String message = isHeartAdded ? "즐겨찾기 성공" : "즐겨찾기 취소 성공";
        ApiUtils.ApiSuccess<?> apiResult = ApiUtils.success(message);

        return ResponseEntity.ok(apiResult);
    }

    @GetMapping("/stores/like")
    public ResponseEntity<?> findAllStores(@AuthenticationPrincipal UserPrincipal userPrincipal){
        LikeStoreResponseDTO.FindAllLikeStoresDTO heartResponseDTO = likeStoreService.findStoresByUserEmail(userPrincipal.getEmail());
        ApiUtils.ApiSuccess<?> apiResult = ApiUtils.success(heartResponseDTO);
        return ResponseEntity.ok(apiResult);

    }



    //특정 store에 좋아요 취소하기
//    @DeleteMapping("/stores/{storeId}/like")
//    public ResponseEntity<?> deleteHeart(@PathVariable(value = "storeId", required = true) Long storeId , @AuthenticationPrincipal UserPrincipal userPrincipal){
//
//    }
}
