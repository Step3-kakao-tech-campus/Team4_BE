package com.ktc.matgpt.review;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ktc.matgpt.TestHelper;
import com.ktc.matgpt.aws.FileValidator;
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.net.URL;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

        //when
        ResultActions resultActions = mvc.perform(
                get("/stores/"+reviewId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        //console
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : "+responseBody);

        // verify
        resultActions.andExpect(jsonPath("$.success").value("true"));
    }

    @DisplayName("임시 리뷰 저장")
    @Test
    public void createTemporaryReview_test() throws Exception {
        //given
        String storeId = "1";
        UserPrincipal mockUserPrincipal = new UserPrincipal(1L, "nstgic3@gmail.com", Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_GUEST")));
        String reviewUuid = "uuiduuiduuiduuid111";
        List<ReviewResponse.UploadS3DTO.PresignedUrlDTO> presignedUrls = new ArrayList<>();

        // 예시 데이터, 실제 데이터는 S3 사전 서명된 URL 생성 로직을 통해 얻어야 함
        String exampleFileName = "image.jpg";
        URL examplePresignedUrl = new URL("https://example-bucket.s3.amazonaws.com/" + exampleFileName + "?AWSAccessKeyId=EXAMPLEKEY&Expires=1570000000&Signature=EXAMPLESIGNATURE");

        // 객체 생성 및 목록에 추가
        presignedUrls.add(new ReviewResponse.UploadS3DTO.PresignedUrlDTO(exampleFileName, examplePresignedUrl));

        when(reviewService.createTemporaryReview(any(Long.class), any(Long.class), any(ReviewRequest.SimpleCreateDTO.class)))
                .thenReturn(reviewUuid);
        when(reviewService.createPresignedUrls(eq(reviewUuid), any(Integer.class)))
                .thenReturn(presignedUrls);

        // 가상의 이미지 파일을 준비합니다.
        MockMultipartFile imageFile = new MockMultipartFile("images", "image.jpg", "image/jpeg", "image content".getBytes());

        // 파일 정보를 맵으로 변환합니다.
        Map<String, String> fileInfo = new HashMap<>();
        fileInfo.put("name", imageFile.getOriginalFilename());
        fileInfo.put("contentType", imageFile.getContentType());

        // 리뷰 데이터 DTO를 준비합니다.
        String requestDTOJson = TestHelper.constructTempReviewCreateDTO();
        MockMultipartFile dataPart = new MockMultipartFile("data", "", "application/json", requestDTOJson.getBytes());



        //when
        ResultActions resultActions = mvc.perform(MockMvcRequestBuilders.multipart("/stores/{storeId}/reviews/temp", 1)
                        .file(dataPart)
                        .file(imageFile)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .with(SecurityMockMvcRequestPostProcessors.user(mockUserPrincipal)))
                        .andExpect(status().isOk());

        //console
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : "+responseBody);

        // verify
        resultActions.andExpect(jsonPath("$.success").value("true"));
    }


    @DisplayName("리뷰 생성 완료") //TODO : 테스트 코드 작성 실패
    @Test
    public void completeReview_test() throws Exception {
        //given
        Long storeId = 1L;
        Long reviewId = 1L;
        String requestDTOJson = TestHelper.constructCompleteDTO();
        String url = String.format("/stores/%d/complete/%d", storeId, reviewId);

        //when
        ResultActions resultActions = mvc.perform(
                post(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(requestDTOJson) // 리뷰 세부 정보 포함
        );

        //console
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : " + responseBody);

        // verify
        resultActions.andExpect(jsonPath("$.success").value("true"));
    }


    @DisplayName("음식점 리뷰 목록(5개 단위) 조회 최신순")
    @Test
    public void findReviewsByStoreIdWithCursorPagingSortByLatest_test() throws Exception {
        //given
        String sortBy = "latest";
        String cursorId = "9";

        //when
        ResultActions resultActions = mvc.perform(
                get("/stores/1/reviews?sortBy="+ sortBy +"&cursorId="+ cursorId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        //console
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : "+responseBody);

        // verify
        resultActions.andExpect(jsonPath("$.success").value("true"));
    }

    @DisplayName("음식점 리뷰 목록(5개 단위) 조회 평점순")
    @Test
    public void findReviewsByStoreIdWithCursorPagingSortByRating_test() throws Exception{
        //given
        String sortBy = "rating";
        String cursorId = "9";
        String cursorRating = "5.0";

        //when
        ResultActions resultActions = mvc.perform(
                get("/stores/1/reviews?sortBy="+ sortBy +"&cursorId="+ cursorId +"&cursorRating="+ cursorRating)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        //console
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : "+responseBody);

        // verify
        resultActions.andExpect(jsonPath("$.success").value("true"));
    }

    @DisplayName("리뷰 수정")
    @Test
    public void updateReview_test() throws Exception{

        //given
        Long reviewId = 1L;
        UserPrincipal mockUserPrincipal = new UserPrincipal(1L, "nstgic3@gmail.com", Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_GUEST")));
        String requestBody = ob.writeValueAsString(new ReviewRequest.UpdateDTO("리뷰-1의 내용이 수정된 결과입니다."));

        //when
        ResultActions resultActions = mvc.perform(
                put("/stores/"+reviewId)
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .with(SecurityMockMvcRequestPostProcessors.user(mockUserPrincipal))
        );

        //console
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : "+responseBody);

        // verify
        resultActions.andExpect(jsonPath("$.success").value("true"));
    }


    @DisplayName("리뷰 삭제")
    @Test
    public void deleteReview_test() throws Exception{
        //given
        Long reviewId = 1L;
        UserPrincipal mockUserPrincipal = new UserPrincipal(1L, "nstgic3@gmail.com", Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_GUEST")));

        //when
        ResultActions resultActions = mvc.perform(
                delete("/stores/"+reviewId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .with(SecurityMockMvcRequestPostProcessors.user(mockUserPrincipal))
        );

        //console
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : "+responseBody);

        // verify
        resultActions.andExpect(jsonPath("$.success").value("true"));
    }

}
