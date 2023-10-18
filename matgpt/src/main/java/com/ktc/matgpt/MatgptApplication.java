package com.ktc.matgpt;

import com.ktc.matgpt.store.Store;
import com.ktc.matgpt.store.StoreJPARepository;
import com.ktc.matgpt.store.entity.Category;
import com.ktc.matgpt.store.entity.SubCategory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import java.text.DecimalFormat;
import java.util.Arrays;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Arrays;

@EnableJpaAuditing
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
                    Store.builder().id(1L).name("미성옥").phoneNumber("02-776-8929").address("서울 중구 명동길 25-11")
                            .businessHours("21:00").latitude(37.5640065).longitude(126.983556)
                            .avgCostPerPerson(11000).avgVisitCount(3).numsOfReview(10).ratingAvg(3.5)
                            .build(),
                    Store.builder().id(2L).name("딘타이펑 명동점").phoneNumber("02-3789-2778").address("서울 중구 명동7길 13 명동증권빌딩")
                            .businessHours("21:00").latitude(37.5643309).longitude(126.984133)
                            .avgCostPerPerson(18000).avgVisitCount(4).numsOfReview(8).ratingAvg(3.8)
                            .build(),
                    Store.builder().id(3L).name("어반플랜트 명동").phoneNumber("0507-1480-0154").address("서울 중구 명동8나길 38 1층")
                            .businessHours("21:00").latitude(37.5614752).longitude(126.982830)
                            .avgCostPerPerson(7000).avgVisitCount(4).numsOfReview(24).ratingAvg(4.2)
                            .build(),
                    Store.builder().id(4L).name("명동교자").phoneNumber("0507-1443-3525").address("서울 중구 명동10길 10 명동교자")
                            .businessHours("21:00").latitude(37.5634232).longitude(126.9850928)
                            .avgCostPerPerson(10000).avgVisitCount(3).numsOfReview(190).ratingAvg(4.0)
                            .build(),
                    Store.builder().id(5L).name("서울지짐이").phoneNumber("02-3789-2778").address("서울 중구 명동9가길10 1, 2층")
                            .businessHours("21:00").latitude(37.5650588).longitude(126.9840605)
                            .avgCostPerPerson(18000).avgVisitCount(4).numsOfReview(81).ratingAvg(3.0)
                            .build(),
                    Store.builder().id(6L).name("흑돈가 명동점").phoneNumber("02-3789-2778").address("서울 중구 명동7길 21")
                            .businessHours("21:00").latitude(37.56471).longitude(126.9838683)
                            .avgCostPerPerson(18000).avgVisitCount(4).numsOfReview(18).ratingAvg(4.4)
                            .build(),
                    Store.builder().id(7L).name("박대감닭한마리").phoneNumber("02-3789-2778").address("서울 중구 명동7길 21")
                            .businessHours("21:00").latitude(37.56471).longitude(126.9812345)
                            .avgCostPerPerson(18000).avgVisitCount(4).numsOfReview(28).ratingAvg(3.3)
                            .build(),
                    Store.builder().id(8L).name("음식점8").phoneNumber("02-3789-2778").address("서울 중구 명동")
                            .businessHours("21:00").latitude(37.568876).longitude(126.9823532)
                            .avgCostPerPerson(18000).avgVisitCount(4).numsOfReview(38).ratingAvg(3.9)
                            .build(),
                    Store.builder().id(9L).name("음식점9").phoneNumber("02-3789-2778").address("서울 중구 명동7길")
                            .businessHours("21:00").latitude(37.56645).longitude(126.988566)
                            .avgCostPerPerson(18000).avgVisitCount(4).numsOfReview(28).ratingAvg(2.7)
                            .build(),
                    Store.builder().id(10L).name("음식점10").phoneNumber("02-3789-2778").address("서울 중구 명동7길 13")
                           .businessHours("21:00").latitude(37.566005).longitude(126.9824525)
                            .avgCostPerPerson(18000).avgVisitCount(4).numsOfReview(548).ratingAvg(2.0)
                            .build(),
                    Store.builder().id(11L).name("음식점11").phoneNumber("02-3789-2778").address("서울 중구")
                           .businessHours("21:00").latitude(37.56313).longitude(126.980006)
                            .avgCostPerPerson(18000).avgVisitCount(4).numsOfReview(23).ratingAvg(4.8)
                            .build()

            ));
        };
    }
}
