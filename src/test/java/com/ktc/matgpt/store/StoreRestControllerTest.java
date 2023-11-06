package com.ktc.matgpt.store;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class StoreRestControllerTest {

    @Autowired
    private MockMvc mvc;

    @DisplayName("storeId로 음식점 조회 성공")
    @Test
    void findById_test() throws Exception {

        // given
        Long storeId = 1L;

        // when
        ResultActions resultActions = performGetRequestWithUrlVariables("/stores/{storeId}", storeId);

        // then
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.data.storeId").value(storeId));
    }

    @DisplayName("storeId로 음식점 조회 실패 - 존재하지 않는 storeId")
    @Test
    void findById_invalidStoreId_test() throws Exception {

        // given
        Long storeId = 0L;

        // when
        ResultActions resultActions = performGetRequestWithUrlVariables("/stores/{storeId}", storeId);

        // then
        resultActions.andExpect(status().is4xxClientError());
        resultActions.andExpect(jsonPath("$.errorCode").value(HttpStatus.NOT_FOUND.value()));
    }

    @DisplayName("storeId로 음식점 조회 실패 - 잘못된 storeId 형식")
    @Test
    void findById_invalidStoreIdFormat_test() throws Exception {

        // given
        String storeId = "abcd";

        // when
        ResultActions resultActions = performGetRequestWithUrlVariables("/stores/{storeId}", storeId);

        // then
        resultActions.andExpect(status().is4xxClientError());
        resultActions.andExpect(jsonPath("$.errorCode").value(HttpStatus.BAD_REQUEST.value()));
    }

    @DisplayName("storeId로 음식점 조회 실패 - 범위를 벗어난 storeId")
    @Test
    void findById_invalidStoreIdLength_test() throws Exception {

        // given
        String storeId = Long.MAX_VALUE + "1234";

        // when
        ResultActions resultActions = performGetRequestWithUrlVariables("/stores/{storeId}", storeId);

        // then
        resultActions.andExpect(status().is4xxClientError());
        resultActions.andExpect(jsonPath("$.errorCode").value(HttpStatus.BAD_REQUEST.value()));
    }

    @DisplayName("위경도 범위 내의 음식점 조회 - 파라미터 x")
    @Test
    void getMarkedStores_withoutQueryParams_test() throws Exception {

        // given

        // when
        ResultActions resultActions = performGetRequest("/stores/boundary");

        // then
        resultActions.andExpect(status().isOk());
    }

    @DisplayName("위경도 범위 내의 음식점 조회 - 파라미터 일부 제공")
    @Test
    void getMarkedStores_withPartialQueryParams_test() throws Exception {

        // given
        Map<String, String> params = new HashMap<>();
        params.put("maxlat", "180.0");
        params.put("minlon", "8");

        // when
        ResultActions resultActions = performGetRequest("/stores/boundary");

        // then
        resultActions.andExpect(status().isOk());
    }

    @DisplayName("위경도 범위 내의 음식점 조회 - 파라미터 모두 제공")
    @Test
    void getMarkedStores_withAllQueryParams_test() throws Exception {

        // given
        Map<String, String> params = new HashMap<>();
        params.put("maxlat", "180.0");
        params.put("maxlon", "180.0");
        params.put("minlat", "0.0");
        params.put("minlon", "0.0");

        // when
        ResultActions resultActions = performGetRequestWithQueryParams("/stores/boundary", params);

        // then
        resultActions.andExpect(status().isOk());
    }

    @DisplayName("위경도 범위 내의 음식점 조회 - 파라미터 범위 초과")
    @Test
    void getMarkedStores_paramOverRange_test() throws Exception {

        // given
        Map<String, String> params = new HashMap<>();
        params.put("maxlat", "200.0");

        // when
        ResultActions resultActions = performGetRequestWithQueryParams("/stores/boundary", params);

        // then
        resultActions.andExpect(status().is4xxClientError());
        resultActions.andExpect(jsonPath("$.errorCode").value(HttpStatus.BAD_REQUEST.value()));
    }

    @DisplayName("위경도 범위 내의 음식점 조회 - 파라미터 범위 미만")
    @Test
    void getMarkedStores_paramUnderRange_test() throws Exception {

        // given
        Map<String, String> params = new HashMap<>();
        params.put("maxlat", "-1.0");

        // when
        ResultActions resultActions = performGetRequestWithQueryParams("/stores/boundary", params);

        // then
        resultActions.andExpect(status().is4xxClientError());
        resultActions.andExpect(jsonPath("$.errorCode").value(HttpStatus.BAD_REQUEST.value()));
    }

    private ResultActions performGetRequest(String urlTemplate) throws Exception {
        return mvc.perform(
                        get(urlTemplate)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

    private ResultActions performGetRequestWithQueryParams(String urlTemplate, Map<String, String> queryParams) throws Exception {
        MockHttpServletRequestBuilder requestBuilder = get(urlTemplate)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        // 쿼리 파라미터 추가
        if (queryParams != null) {
            queryParams.forEach(requestBuilder::queryParam);
        }

        return mvc.perform(requestBuilder).andDo(print());
    }

    private ResultActions performGetRequestWithUrlVariables(String urlTemplate, Object... uriVariables) throws Exception {
        return mvc.perform(
                        get(urlTemplate, uriVariables)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(print());
    }
}
