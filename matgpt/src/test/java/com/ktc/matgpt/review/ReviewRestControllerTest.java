package com.ktc.matgpt.review;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ktc.matgpt.TestHelper;
import com.ktc.matgpt.exception.ErrorCode;
import com.ktc.matgpt.food.FoodService;
import com.ktc.matgpt.image.ImageService;
import com.ktc.matgpt.review.dto.ReviewRequest;
import com.ktc.matgpt.review.dto.ReviewResponse;
import com.ktc.matgpt.security.UserPrincipal;
import com.ktc.matgpt.store.StoreService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.net.URL;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@Sql(value = "classpath:custom_modified.sql")
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("dev")
public class ReviewRestControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper ob;

    @Mock
    private ReviewService reviewService;

    @Mock
    private ImageService imageService;

    @Mock
    private FoodService foodService;

    @Mock
    private StoreService storeService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }



    @DisplayName("개별 리뷰 상세조회")
    @Test
    public void findReviewById_test() throws Exception {
        //given
        String reviewId = "1";
        String storeId = "1";
        UserPrincipal mockUserPrincipal = new UserPrincipal(1L, "nstgic3@gmail.com", false, Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_GUEST")));

        //when
        ResultActions resultActions = mvc.perform(
                get("/stores/"+ storeId +"/reviews/"+reviewId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .with(SecurityMockMvcRequestPostProcessors.user(mockUserPrincipal))
        );

        //console
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : "+responseBody);

        // verify
        resultActions.andExpect(jsonPath("$.data.reviewId").value("1"));
        resultActions.andExpect(jsonPath("$.data.storeId").value("1"));
        resultActions.andExpect(jsonPath("$.data.reviewer.email").value("nstgic3@gmail.com"));
        resultActions.andExpect(jsonPath("$.data.createdAt").exists());
        resultActions.andExpect(jsonPath("$.data.averageCostPerPerson").value(25000));
        resultActions.andExpect(jsonPath("$.data.peopleCount").value(2));
        resultActions.andExpect(jsonPath("$.data.rating").value(5));
        resultActions.andExpect(jsonPath("$.data.recommendCount").value(2));
        resultActions.andExpect(jsonPath("$.data.reviewImages[0].image").value("image1.png"));
        resultActions.andExpect(jsonPath("$.data.reviewImages[0].tags[0].name").value("food1"));
        resultActions.andExpect(jsonPath("$.data.reviewImages[0].tags[0].location_x").value(25.08));
        resultActions.andExpect(jsonPath("$.data.reviewImages[0].tags[0].location_y").value(36.74));
        resultActions.andExpect(jsonPath("$.data.reviewImages[0].tags[0].rating").value(3));
        resultActions.andExpect(jsonPath("$.data.totalPrice").value(50000));
        resultActions.andExpect(jsonPath("$.data.updated").value(true));
        resultActions.andExpect(jsonPath("$.data.owner").value(true));
    }


    @DisplayName("임시 리뷰 저장")
    @Test
    public void createTemporaryReview_test() throws Exception {
        //given
        String storeId = "1";
        UserPrincipal mockUserPrincipal = new UserPrincipal(1L, "nstgic3@gmail.com", false, Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_GUEST")));
        String reviewUuid = "uuiduuiduuiduuid111";
        List<ReviewResponse.UploadS3DTO.PresignedUrlDTO> presignedUrls = new ArrayList<>();

        // 예시 데이터, 실제 데이터는 S3 사전 서명된 URL 생성 로직을 통해 얻어야 함
        String exampleFileName = "image.jpg";
        URL examplePresignedUrl = new URL("https://example-bucket.s3.amazonaws.com/" + exampleFileName + "?AWSAccessKeyId=EXAMPLEKEY&Expires=1570000000&Signature=EXAMPLESIGNATURE");


        // 객체 생성 및 목록에 추가
        presignedUrls.add(new ReviewResponse.UploadS3DTO.PresignedUrlDTO(examplePresignedUrl));

        when(reviewService.createPresignedUrls(eq(reviewUuid), any(Integer.class)))
                .thenReturn(presignedUrls);

        // 가상의 이미지 파일을 준비합니다.
        // 리뷰 데이터 DTO를 준비합니다.
        String requestDTOJson = TestHelper.constructTempReviewCreateDTO();

        //when - 임시 리뷰 저장을 수행합니다.
        ResultActions resultActions = mvc.perform(
                post("/stores/"+ storeId +"/reviews/temp")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(requestDTOJson)
                        .with(SecurityMockMvcRequestPostProcessors.user(mockUserPrincipal)))
                        .andExpect(status().isOk());

        //console
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : "+responseBody);

        // verify - 임시 리뷰 저장의 응답을 검증합니다.
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.data.reviewId").exists());
        resultActions.andExpect(jsonPath("$.data.presignedUrls[0].presignedUrl").exists());
        resultActions.andExpect(jsonPath("$.errorCode").doesNotExist());

        // given
        int reviewId = 20; // custom_modified.sql 기반 reviewId: 1~19까지 존재

        //when - 2. 저장된 임시 리뷰 조회
        resultActions = mvc.perform(
                get("/stores/"+ storeId +"/reviews/"+reviewId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .with(SecurityMockMvcRequestPostProcessors.user(mockUserPrincipal))
        );

        //console
        responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : " + responseBody);

        // verify - TestHelper의 임시 리뷰 데이터 기반 검증
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.data.reviewId").value(20));
        resultActions.andExpect(jsonPath("$.data.storeId").value(1));
        resultActions.andExpect(jsonPath("$.data.content").value("이것은 테스트 리뷰입니다."));
        resultActions.andExpect(jsonPath("$.data.rating").value(5));
        resultActions.andExpect(jsonPath("$.data.peopleCount").value(2));
        resultActions.andExpect(jsonPath("$.data.totalPrice").value(50000));
        resultActions.andExpect(jsonPath("$.errorCode").doesNotExist());
    }


    @DisplayName("전체 리뷰 저장 완료")
    @Test
    public void completeReview_test() throws Exception {
        //given
        // custom_modified.sql로 저장되어 있는 기존 리뷰 reviewId:2 (storeId:1, userId:2)에 Tag, Image 등록하기
        Long storeId = 1L;
        Long reviewId = 2L;
        String requestDTOJson = TestHelper.constructCompleteDTO();
        UserPrincipal mockUserPrincipal = new UserPrincipal(2L, "female@gmail.com", false, Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_GUEST")));
        String url = String.format("/stores/%d/reviews/%d", storeId, reviewId);
        String successMsg = "리뷰가 성공적으로 완료되었습니다.";

        //when - 1. 전체 리뷰 저장
        ResultActions resultActions = mvc.perform(
                post(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(requestDTOJson) // 리뷰 세부 정보 포함
                .with(SecurityMockMvcRequestPostProcessors.user(mockUserPrincipal))
        );

        //console
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : " + responseBody);

        // verify
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.data").value(successMsg));

        //when - 2. 저장 완료된 리뷰 조회(Image, Tag가 저장됐는지 확인)
        resultActions = mvc.perform(
                get(url)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .with(SecurityMockMvcRequestPostProcessors.user(mockUserPrincipal))
        );

        //console
        responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : " + responseBody);

        // verify
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.data.reviewId").value(2));
        resultActions.andExpect(jsonPath("$.data.storeId").value(1));
        // TestHelper의 Image, Tag 데이터 기반 검증
        resultActions.andExpect(jsonPath("$.data.reviewImages[0].image").value("https://example.com/image1.jpg"));
        resultActions.andExpect(jsonPath("$.data.reviewImages[0].tags[0].name").value("짜장면"));
        resultActions.andExpect(jsonPath("$.data.reviewImages[0].tags[0].location_x").value(50));
        resultActions.andExpect(jsonPath("$.data.reviewImages[0].tags[0].location_y").value(100));
        resultActions.andExpect(jsonPath("$.data.reviewImages[0].tags[0].rating").value(3));
        resultActions.andExpect(jsonPath("$.data.reviewImages[0].tags[1].name").value("짬뽕"));
        resultActions.andExpect(jsonPath("$.data.reviewImages[0].tags[2].name").value("탕수육"));
        // custom_modified.sql의 기존 reviewId:2 리뷰 기반 검증
        resultActions.andExpect(jsonPath("$.data.content").value("참말로 맛있네용"));
        resultActions.andExpect(jsonPath("$.data.peopleCount").value(2));
        resultActions.andExpect(jsonPath("$.data.totalPrice").value(30000));
        resultActions.andExpect(jsonPath("$.errorCode").doesNotExist());
    }


    @DisplayName("음식점 리뷰 목록(8개 단위) 조회 최신순")
    @Test
    public void findReviewsByStoreIdWithCursorPagingSortByLatest_test() throws Exception {
        //given
        // storeId 1에 등록된 리뷰는 총 11개(custom_modified.sql)
        String storeId = "1";
        String sortBy = "latest";

        //when - 최초 조회 요청 (cursorId 없을 경우 default -> 자동으로 max값)
        ResultActions resultActions = mvc.perform(
                get("/stores/"+ storeId +"/reviews?sortBy="+ sortBy)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        //console
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : "+responseBody);

        // verify
        // 리뷰 데이터 응답 검증
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.data.body[0].reviewId").value("11"));
        resultActions.andExpect(jsonPath("$.data.body[1].reviewId").value("10"));
        resultActions.andExpect(jsonPath("$.data.body[2].reviewId").value("9"));
        resultActions.andExpect(jsonPath("$.data.body[3].reviewId").value("8"));
        resultActions.andExpect(jsonPath("$.data.body[4].reviewId").value("7"));
        resultActions.andExpect(jsonPath("$.data.body[5].reviewId").value("6"));
        resultActions.andExpect(jsonPath("$.data.body[6].reviewId").value("5"));
        resultActions.andExpect(jsonPath("$.data.body[7].reviewId").value("4"));
        // 페이징 관련 데이터 응답 검증
        resultActions.andExpect(jsonPath("$.data.paging.hasNext").value(true));
        resultActions.andExpect(jsonPath("$.data.paging.nextCursorId").value(4));

        //given
        Long cursorId = 4L;

        //when - 2차 요청 (cursor는 이전 요청의 마지막 리뷰 id)
        resultActions = mvc.perform(
                get("/stores/"+ storeId +"/reviews?sortBy="+ sortBy +"&cursorId="+ cursorId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );
        // verify
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.data.body[0].reviewId").value("3"));
        resultActions.andExpect(jsonPath("$.data.body[1].reviewId").value("2"));
        resultActions.andExpect(jsonPath("$.data.body[2].reviewId").value("1"));
        // 페이징 관련 데이터 응답 검증
        resultActions.andExpect(jsonPath("$.data.paging.hasNext").value(false));
        resultActions.andExpect(jsonPath("$.data.paging.nextCursorId").value(1));
    }

    @DisplayName("음식점 리뷰 목록(8개 단위) 조회 추천순")
    @Test
    public void findReviewsByStoreIdWithCursorPagingSortByLikes_test() throws Exception{
        //given
        // storeId 1에 등록된 리뷰는 총 11개(custom_modified.sql)
        String storeId = "1";
        String sortBy = "likes";


        //when - 최초 요청 (cursorId, cursorLikes 없을 경우 default -> 자동으로 max값)
        ResultActions resultActions = mvc.perform(
                get("/stores/"+ storeId +"/reviews?sortBy="+ sortBy)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        //console
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : "+responseBody);

        // verify
        // 리뷰 데이터 응답 검증
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.data.body[0].reviewId").value(11));
        resultActions.andExpect(jsonPath("$.data.body[0].numOfLikes").value(5));
        resultActions.andExpect(jsonPath("$.data.body[1].reviewId").value(9));
        resultActions.andExpect(jsonPath("$.data.body[1].numOfLikes").value(3));
        resultActions.andExpect(jsonPath("$.data.body[2].reviewId").value(2));
        resultActions.andExpect(jsonPath("$.data.body[2].numOfLikes").value(3));
        resultActions.andExpect(jsonPath("$.data.body[3].reviewId").value(1));
        resultActions.andExpect(jsonPath("$.data.body[3].numOfLikes").value(2));
        resultActions.andExpect(jsonPath("$.data.body[4].reviewId").value(7));
        resultActions.andExpect(jsonPath("$.data.body[4].numOfLikes").value(1));
        resultActions.andExpect(jsonPath("$.data.body[5].reviewId").value(3));
        resultActions.andExpect(jsonPath("$.data.body[5].numOfLikes").value(1));
        resultActions.andExpect(jsonPath("$.data.body[6].reviewId").value(10));
        resultActions.andExpect(jsonPath("$.data.body[6].numOfLikes").value(0));
        resultActions.andExpect(jsonPath("$.data.body[7].reviewId").value(8));
        resultActions.andExpect(jsonPath("$.data.body[7].numOfLikes").value(0));
        // 페이징 관련 데이터 응답 검증
        resultActions.andExpect(jsonPath("$.data.paging.hasNext").value(true));
        resultActions.andExpect(jsonPath("$.data.paging.nextCursorId").value(8));
        resultActions.andExpect(jsonPath("$.data.paging.nextCursor").value(0));

        //given - 2차 요청 (cursor는 이전 요청의 마지막 리뷰 id, numOfLikes)
        Long cursorId = 8L;
        Integer cursor = 0;
        //when
        resultActions = mvc.perform(
                get("/stores/"+ storeId +"/reviews?sortBy="+ sortBy +"&cursorId="+ cursorId +"&cursor="+ cursor)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );
        //console
        responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : "+responseBody);

        // verify
        // 리뷰 데이터 응답 검증
        resultActions.andExpect(jsonPath("$.data.body[0].reviewId").value(6));
        resultActions.andExpect(jsonPath("$.data.body[0].numOfLikes").value(0));
        resultActions.andExpect(jsonPath("$.data.body[1].reviewId").value(5));
        resultActions.andExpect(jsonPath("$.data.body[1].numOfLikes").value(0));
        resultActions.andExpect(jsonPath("$.data.body[2].reviewId").value(4));
        resultActions.andExpect(jsonPath("$.data.body[2].numOfLikes").value(0));
        // 페이징 관련 데이터 응답 검증
        resultActions.andExpect(jsonPath("$.data.paging.hasNext").value(false));
        resultActions.andExpect(jsonPath("$.data.paging.nextCursorId").value(4));
        resultActions.andExpect(jsonPath("$.data.paging.nextCursor").value(0));
    }

    @DisplayName("리뷰 수정")
    @Test
    public void updateReview_test() throws Exception{

        //given
        String storeId = "1";
        String reviewId = "1";
        String content = "리뷰-1의 내용이 수정된 결과입니다.";
        String successMsg = "리뷰 내용이 수정되었습니다.";

        UserPrincipal mockUserPrincipal = new UserPrincipal(1L, "nstgic3@gmail.com", false, Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_GUEST")));

        String requestBody = ob.writeValueAsString(new ReviewRequest.UpdateDTO(content));

        //when - 1. 리뷰 수정 수행
        ResultActions resultActions = mvc.perform(
                put("/stores/"+ storeId +"/reviews/"+reviewId)
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .with(SecurityMockMvcRequestPostProcessors.user(mockUserPrincipal))
        );
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : "+responseBody);

        // verify
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.data").value(successMsg));

        // when - 2. 수정한 리뷰 조회
        resultActions = mvc.perform(
                get("/stores/"+ storeId +"/reviews/"+reviewId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .with(SecurityMockMvcRequestPostProcessors.user(mockUserPrincipal))
        );
        //console
        responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : "+responseBody);
        // verify
        resultActions.andExpect(jsonPath("$.data.content").value(content));
    }


    @DisplayName("리뷰 삭제")
    @Test
    public void deleteReview_test() throws Exception{
        //given
        String storeId = "1";
        String reviewId = "1";
        String successMsg = "리뷰가 삭제되었습니다.";

        UserPrincipal mockUserPrincipal = new UserPrincipal(1L, "nstgic3@gmail.com", false, Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_GUEST")));

        //when - 1. 리뷰 삭제 수행
        ResultActions resultActions = mvc.perform(
                delete("/stores/"+ storeId +"/reviews/"+reviewId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .with(SecurityMockMvcRequestPostProcessors.user(mockUserPrincipal))
        );

        //console
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : "+responseBody);

        // verify
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.data").value(successMsg));

        //when - 2. 삭제된 리뷰 id 조회
        resultActions = mvc.perform(
                get("/stores/"+ storeId +"/reviews/"+reviewId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .with(SecurityMockMvcRequestPostProcessors.user(mockUserPrincipal))
        );
        //console
        responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : "+responseBody);
        // verify
        resultActions.andExpect(jsonPath("$.errorCode").value(404));
        resultActions.andExpect(jsonPath("$.message").value(ErrorCode.REVIEW_NOT_FOUND.getMessage()));
    }
}
