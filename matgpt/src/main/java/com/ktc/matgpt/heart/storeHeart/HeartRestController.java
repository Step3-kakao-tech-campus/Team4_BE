package com.ktc.matgpt.heart.storeHeart;


import com.ktc.matgpt.security.UserPrincipal;
import com.ktc.matgpt.utils.ApiUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;



@RestController
@RequiredArgsConstructor
@RequestMapping("")
public class HeartRestController {

    private final HeartService heartService;

    @GetMapping("/stores/like")
    public ResponseEntity<?> findAllStores(@AuthenticationPrincipal UserPrincipal userPrincipal){
        HeartResponseDTO.FindAllLikeStoresDTO heartResponseDTO = heartService.findStoresByUserEmail(userPrincipal.getEmail());
        ApiUtils.ApiSuccess<?> apiResult = ApiUtils.success(heartResponseDTO);
        return ResponseEntity.ok(apiResult);

    }

    //특정 store에 좋아요 누르기
    @PostMapping("/stores/{storeId}/like")
    public ResponseEntity<?> addHeart(@PathVariable(value = "storeId", required = true) Long storeId , @AuthenticationPrincipal UserPrincipal userPrincipal) {
        heartService.addHeartToStore(storeId, userPrincipal.getEmail());
        ApiUtils.ApiSuccess<?> apiResult = ApiUtils.success("success");
        return ResponseEntity.ok(apiResult);
    }


    //특정 store에 좋아요 취소하기
//    @DeleteMapping("/stores/{storeId}/like")
//    public ResponseEntity<?> deleteHeart(@PathVariable(value = "storeId", required = true) Long storeId , @AuthenticationPrincipal UserPrincipal userPrincipal){
//
//    }
}
