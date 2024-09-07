package FITPET.dev;

import FITPET.dev.common.enums.InquiryStatus;
import FITPET.dev.common.security.jwt.JwtTokenProvider;
import FITPET.dev.controller.InquiryController;
import FITPET.dev.dto.request.InquiryRequest;
import FITPET.dev.dto.response.InquiryResponse;
import FITPET.dev.service.InquiryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureRestDocs
@WebMvcTest(InquiryController.class)
@AutoConfigureMockMvc(addFilters = false)
public class InquiryControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @MockBean
    private InquiryService inquiryService;

    @MockBean
    private JpaMetamodelMappingContext jpaMappingContext;

    @Test
    @DisplayName("/inquiry 로 이름, 이메일, 전화번호, 문의 내역 정보를 보내 Inquiry 객체를 생성 후 저장한다.")
    public void createInquiry() throws Exception {

        // given
        InquiryRequest.InquiryDto request = new InquiryRequest.InquiryDto("견적 문의", "test@naver.com",
                "010-1234-1234", "1:1 문의 생성 테스트");

        given(inquiryService.postInquiry(any()))
                .willReturn(new InquiryResponse.InquiryDto(1L, "", "견적 문의", "test@naver.com",
                        "010-1234-1234", "1:1 문의 생성 테스트", InquiryStatus.PENDING.getLabel()));

        // when & then
        mockMvc.perform(post("/api/v1/inquiry")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value("SUCCESS"))
                .andExpect(jsonPath("$.statusCode").value("200"))
                .andExpect(jsonPath("$.message").value("1:1 문의 전송에 성공했습니다."))
                .andExpect(jsonPath("$.data.inquiryId").value(1))
                .andExpect(jsonPath("$.data.createdAt").value(""))
                .andExpect(jsonPath("$.data.name").value("견적 문의"))
                .andExpect(jsonPath("$.data.email").value("test@naver.com"))
                .andExpect(jsonPath("$.data.phoneNum").value("010-1234-1234"))
                .andExpect(jsonPath("$.data.comment").value("1:1 문의 생성 테스트"))
                .andExpect(jsonPath("$.data.status").value("답변 대기"))
                .andDo(document("post-inquiry",
                        requestFields(
                                fieldWithPath("name").description("문의자 성명"),
                                fieldWithPath("email").description("문의 답변을 받을 이메일 주소"),
                                fieldWithPath("phoneNum").description("문의자 전화번호"),
                                fieldWithPath("comment").description("문의 내용")
                        ),
                        responseFields(
                                fieldWithPath("result").description("결과 성공 여부"),
                                fieldWithPath("statusCode").description("상태 코드"),
                                fieldWithPath("message").description("결과 메시지"),
                                fieldWithPath("data.inquiryId").description("생성된 문의의 ID"),
                                fieldWithPath("data.createdAt").description("생성 시간"),
                                fieldWithPath("data.name").description("생성된 문의의 문의자 이름"),
                                fieldWithPath("data.email").description("생성된 문의의 답변을 받을 이메일 주소"),
                                fieldWithPath("data.phoneNum").description("생성된 문의의 문의자 전화번호"),
                                fieldWithPath("data.comment").description("생성된 문의의 문의 내용"),
                                fieldWithPath("data.status").description("생성된 문의의 상태")
                        )
                ));
    }
}