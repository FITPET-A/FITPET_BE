package FITPET.dev.dto.response;

import FITPET.dev.common.enums.InquiryStatus;
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
        private String createdAt;
        private String name;
        private String email;
        private String phoneNum;
        private String comment;
        private String status;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InquiryListDto {
        private List<InquiryDto> inquiryDtoList;
    }
}
