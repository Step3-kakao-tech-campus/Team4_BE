package com.ktc.matgpt;

import com.ktc.matgpt.store.Store;
import com.ktc.matgpt.store.StoreJPARepository;
import com.ktc.matgpt.store.entity.Category;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import java.text.DecimalFormat;
import java.util.Arrays;

@SpringBootApplication
public class MatgptApplication {

    public static void main(String[] args) {
        SpringApplication.run(MatgptApplication.class, args);
    }
    @Profile("dev")
    @Bean
    CommandLineRunner localServerStart(StoreJPARepository storeJPARepository){
        return args -> {
            storeJPARepository.saveAll(Arrays.asList(
                    Store.builder()
                            .id(1L)
                            .storeName("미성옥")
                            .number("02-776-8929")
                            .address("서울 중구 명동길 25-11")
                            .category(Category.KOREAN)
                            .detailedCategory("설렁탕/곰탕")
                            .openingTime("06:00")
                            .closingTime("21:00")
                            .latitude("126.983556")
                            .longitude("37.5640065")
                            .avgCostPerPerson(11000)
                            .avgVisitCount(3)
                            .numsOfReview(10)
                            .ratingAvg(3.5)
                            .build(),
                    Store.builder()
                            .id(2L)
                            .storeName("딘타이펑 명동점")
                            .number("02-3789-2778")
                            .address("서울 중구 명동7길 13 명동증권빌딩")
                            .category(Category.CHINESE)
                            .detailedCategory("중식당")
                            .openingTime("11:00")
                            .closingTime("21:00")
                            .latitude("126.984133")
                            .longitude("37.5643309")
                            .avgCostPerPerson(18000)
                            .avgVisitCount(4)
                            .numsOfReview(8)
                            .ratingAvg(3.8)
                            .build(),
                    Store.builder()
                            .id(3L)
                            .storeName("어반플랜트 명동")
                            .number("0507-1480-0154")
                            .address("서울 중구 명동8나길 38 1층")
                            .category(Category.DESSERT)
                            .detailedCategory("디저트/캎")
                            .openingTime("09:00")
                            .closingTime("21:00")
                            .latitude("37.5614752")
                            .longitude("126.982830")
                            .avgCostPerPerson(7000)
                            .avgVisitCount(4)
                            .numsOfReview(24)
                            .ratingAvg(4.2)
                            .build(),
                    Store.builder()
                            .id(4L)
                            .storeName("명동교자")
                            .number("0507-1443-3525")
                            .address("서울 중구 명동10길 10 명동교자")
                            .category(Category.KOREAN)
                            .detailedCategory("칼국수/만두")
                            .openingTime("10:30")
                            .closingTime("21:00")
                            .latitude("37.5634232")
                            .longitude("126.9850928")
                            .avgCostPerPerson(10000)
                            .avgVisitCount(3)
                            .numsOfReview(190)
                            .ratingAvg(4.0)
                            .build()

            ));
        };
    }
}
