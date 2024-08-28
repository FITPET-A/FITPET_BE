package FITPET.dev.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

public class InquiryResponse {
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InquiryDto {
        private Long inquiryId;
        private String name;
        private String email;
        private String phoneNum;
        private String comment;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InquiryListDto {
        private List<InquiryDto> inquiryDtoList;
    }
}
