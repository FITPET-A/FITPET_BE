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

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InquiryDetailDto {
        private Long inquiryId;
        private String name;
        private String email;
        private String phoneNum;
        private String comment;
        private InquiryStatus status;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InquiryPageDto {
        private List<InquiryDto> content;
        private int currentPage; // 현재 페이지 번호
        private int pageSize; // 페이지 크기
        private int numberOfElement; // 현재 페이지의 element 개수
        private int totalElements; // 전체 element 개수
        private int totalPage; // 전체 페이지 개수
    }
}
