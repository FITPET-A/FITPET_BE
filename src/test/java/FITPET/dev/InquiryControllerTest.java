package FITPET.dev;

import FITPET.dev.controller.InquiryController;
import FITPET.dev.dto.request.InquiryRequest;
import FITPET.dev.entity.Inquiry;
import FITPET.dev.service.InquiryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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
public class InquiryControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InquiryService inquiryService;

    @Test
    @DisplayName("/inquiry 로 이름, 이메일, 전화번호, 문의 내역 정보를 보내 Inquiry 객체를 생성 후 저장한다.")
    public void createInquiry() throws Exception {
        // given
        InquiryRequest.InquiryDto request = new InquiryRequest.InquiryDto("견적 문의", "test@naver.com",
                "010-1234-1234", "1:1 문의 생성 테스트");

        given(inquiryService.postInquiry(any()))
                .willReturn(new Inquiry(1L, "견적 문의", "test@naver.com",
                        "010-1234-1234", "1:1 문의 생성 테스트"));

        // when & then
        mockMvc.perform(post("/inquiry")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(header().string("location", "/inquiry"))
                .andExpect(jsonPath("$.statusCode").value("200"))
                .andExpect(jsonPath("$.message").value("1:1 문의 전송에 성공했습니다."))
                .andExpect(jsonPath("$.result.inquiryId").value(1))
                .andExpect(jsonPath("$.result.name").value("견적 문의"))
                .andExpect(jsonPath("$.result.email").value("test@naver.com"))
                .andExpect(jsonPath("$.result.phoneNum").value("010-1234-1234"))
                .andExpect(jsonPath("$.result.comment").value("1:1 문의 생성 테스트"))
                .andDo(document("post-inquiry",
                        requestFields(
                                fieldWithPath("name").description("문의자 성명"),
                                fieldWithPath("email").description("문의 답변을 받을 이메일 주소"),
                                fieldWithPath("phoneNum").description("문의자 전화번호"),
                                fieldWithPath("comment").description("문의 내용")
                        ),
                        responseFields(
                                fieldWithPath("statusCode").description("상태 코드"),
                                fieldWithPath("message").description("결과 메시지"),
                                fieldWithPath("result.inquiryId").description("생성된 문의의 ID"),
                                fieldWithPath("result.name").description("생성된 문의의 문의자 이름"),
                                fieldWithPath("result.email").description("생성된 문의의 답변을 받을 이메일 주소"),
                                fieldWithPath("result.phoneNum").description("생성된 문의의 문의자 전화번호"),
                                fieldWithPath("result.comment").description("생성된 문의의 문의 내용")
                        )
                ));
    }
}
      