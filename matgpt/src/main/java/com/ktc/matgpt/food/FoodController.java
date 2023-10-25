package com.ktc.matgpt.food;

import com.ktc.matgpt.food.dto.FoodDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/stores/{storeId}/foods")
@RequiredArgsConstructor
public class FoodController {

    private final FoodService foodService;

    // 음식 목록 제공
    @GetMapping
    public ResponseEntity<List<Food>> getFoodsByStore(@PathVariable Long storeId) {
        List<Food> foods = foodService.getFoodsByStore(storeId);
        return ResponseEntity.ok(foods);
    }

    // 새로운 음식 추가
    @PostMapping
//    @PreAuthorize("hasRole('USER_ADMIN')") //TODO: 우수 회원에 한해서 등록 허용할건지
    public ResponseEntity<Food> addNewFoodToStore(@PathVariable Long storeId, @RequestBody FoodDTO.CreateDTO foodDTO) {
        Food newFood = foodService.addFoodToStore(storeId, foodDTO);
        return new ResponseEntity<>(newFood, HttpStatus.CREATED);
    }
}
