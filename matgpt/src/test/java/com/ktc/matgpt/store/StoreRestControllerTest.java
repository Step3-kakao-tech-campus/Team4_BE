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
