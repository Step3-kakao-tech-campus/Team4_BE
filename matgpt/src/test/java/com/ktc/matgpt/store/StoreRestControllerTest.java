package com.ktc.matgpt.store;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.ktc.matgpt.store.entity.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.Arrays;

import static org.springframework.security.config.http.MatcherType.mvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class StoreRestControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper ob;

    @Autowired
    private StoreJPARepository storeJPARepository;

    @BeforeEach
    void setUp(){
        storeJPARepository.saveAll(Arrays.asList(
                Store.builder().id(1L).storeName("미성옥").number("02-776-8929").address("서울 중구 명동길 25-11").category(Category.KOREAN)
                        .detailedCategory("설렁탕/곰탕").openingTime("06:00").closingTime("21:00").latitude("37.5640065").longitude("126.983556")
                        .avgCostPerPerson(11000).avgVisitCount(3).numsOfReview(10).ratingAvg(3.5)
                        .build(),
                Store.builder().id(2L).storeName("딘타이펑 명동점").number("02-3789-2778").address("서울 중구 명동7길 13 명동증권빌딩").category(Category.CHINESE)
                        .detailedCategory("중식당").openingTime("11:00").closingTime("21:00").latitude("37.5643309").longitude("126.984133")
                        .avgCostPerPerson(18000).avgVisitCount(4).numsOfReview(8).ratingAvg(3.8)
                        .build(),
                Store.builder().id(3L).storeName("어반플랜트 명동").number("0507-1480-0154").address("서울 중구 명동8나길 38 1층").category(Category.DESSERT)
                        .detailedCategory("디저트/캎").openingTime("09:00").closingTime("21:00").latitude("37.5614752").longitude("126.982830")
                        .avgCostPerPerson(7000).avgVisitCount(4).numsOfReview(24).ratingAvg(4.2)
                        .build(),
                Store.builder().id(4L).storeName("명동교자").number("0507-1443-3525").address("서울 중구 명동10길 10 명동교자").category(Category.KOREAN)
                        .detailedCategory("칼국수/만두").openingTime("10:30").closingTime("21:00").latitude("37.5634232").longitude("126.9850928")
                        .avgCostPerPerson(10000).avgVisitCount(3).numsOfReview(190).ratingAvg(4.0)
                        .build(),
                Store.builder().id(5L).storeName("서울지짐이").number("02-3789-2778").address("서울 중구 명동9가길10 1, 2층").category(Category.KOREAN)
                        .detailedCategory("전").openingTime("11:00").closingTime("21:00").latitude("37.5650588").longitude("126.9840605")
                        .avgCostPerPerson(18000).avgVisitCount(4).numsOfReview(81).ratingAvg(3.0)
                        .build(),
                Store.builder().id(6L).storeName("흑돈가 명동점").number("02-3789-2778").address("서울 중구 명동7길 21").category(Category.KOREAN)
                        .detailedCategory("돼지고기").openingTime("11:00").closingTime("21:00").latitude("37.56471").longitude("126.9838683")
                        .avgCostPerPerson(18000).avgVisitCount(4).numsOfReview(18).ratingAvg(4.4)
                        .build(),
                Store.builder().id(7L).storeName("박대감닭한마리").number("02-3789-2778").address("서울 중구 명동7길 21").category(Category.KOREAN)
                        .detailedCategory("칼국수").openingTime("11:00").closingTime("21:00").latitude("37.56471").longitude("126.9812345")
                        .avgCostPerPerson(18000).avgVisitCount(4).numsOfReview(28).ratingAvg(3.3)
                        .build(),
                Store.builder().id(8L).storeName("음식점8").number("02-3789-2778").address("서울 중구 명동").category(Category.KOREAN)
                        .detailedCategory("국밥").openingTime("11:00").closingTime("21:00").latitude("37.568876").longitude("126.9823532")
                        .avgCostPerPerson(18000).avgVisitCount(4).numsOfReview(38).ratingAvg(3.9)
                        .build(),
                Store.builder().id(9L).storeName("음식점9").number("02-3789-2778").address("서울 중구 명동7길").category(Category.KOREAN)
                        .detailedCategory("퓨전").openingTime("11:00").closingTime("21:00").latitude("37.56645").longitude("126.988566")
                        .avgCostPerPerson(18000).avgVisitCount(4).numsOfReview(28).ratingAvg(2.7)
                        .build(),
                Store.builder().id(10L).storeName("음식점10").number("02-3789-2778").address("서울 중구 명동7길 13").category(Category.CHINESE)
                        .detailedCategory("짜장면").openingTime("11:00").closingTime("21:00").latitude("37.566005").longitude("126.9824525")
                        .avgCostPerPerson(18000).avgVisitCount(4).numsOfReview(548).ratingAvg(2.0)
                        .build(),
                Store.builder().id(11L).storeName("음식점11").number("02-3789-2778").address("서울 중구").category(Category.JAPANESE)
                        .detailedCategory("스시").openingTime("11:00").closingTime("21:00").latitude("37.56313").longitude("126.980006")
                        .avgCostPerPerson(18000).avgVisitCount(4).numsOfReview(23).ratingAvg(4.8)
                        .build()

        ));
    }

    @DisplayName("all_stores_test")
    @Test
    public void findAllStoreWithoutPaging_test() throws Exception {
        //given

        //when
//        ResultActions resultActions = mvc.perform(
//                get("/stores").contentType(MediaType.APPLICATION_JSON_VALUE)
//
//        );
//
//        //console
//        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
//        System.out.println("테스트 : "+responseBody);
    }

    @DisplayName("id 순으로 정렬한 stores")
    @Test
    public void findAllStoreOrderByIdWithPaging_test() throws Exception{
        //given
        String sort = "id";
        String cursor = "14";

        //when
        ResultActions resultActions = mvc.perform(
                get("/stores?sort="+sort+"&cursor="+cursor).contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        //console
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : "+responseBody);
    }

    @DisplayName("별점 순으로 정렬한 stores")
    @Test
    public void findAllStoreOrderByRatingWithPaging_test() throws Exception{
        //given
        String sort = "rating";
        String cursor = "11";

        //when
        ResultActions resultActions = mvc.perform(
                get("/stores?sort="+sort+"&cursor="+cursor).contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        //console
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : "+responseBody);

    }

    @DisplayName("review 갯수로 정렬한 stores")
    @Test
    public void findAllStoreOrderByReviewsWithPaging_test() throws Exception{
        //given
        String sort = "review";
        String cursor = "6";

        //when
        ResultActions resultActions = mvc.perform(
                get("/stores?sort="+sort+"&cursor="+cursor).contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        //console
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : "+responseBody);

        //console

    }





    @DisplayName("specific_store_information_test")
    @Test
    public void findById_test() throws Exception{

        //given
        Long id = 1L;
        //when
        ResultActions resultActions = mvc.perform(
                get("/stores/"+id).contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        //console
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : "+responseBody);
    }

}
