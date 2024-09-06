package FITPET.dev.dto.response;

import FITPET.dev.common.annotation.ExcelColumn;
import FITPET.dev.common.annotation.ExcelFile;
import java.util.List;
import FITPET.dev.common.enums.ComparisonStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class ComparisonResponse {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @ExcelFile
    public static class ComparisonExcelDto {
        @ExcelColumn(headerName = "견적서Id")
        private Long comparisonId;
        @ExcelColumn(headerName = "견적 요청 일시")
        private String createdAt;
        @ExcelColumn(headerName = "품종")
        private String petType;
        @ExcelColumn(headerName = "상세 품종")
        private String petSpecies;
        @ExcelColumn(headerName = "이름")
        private String petName;
        @ExcelColumn(headerName = "나이")
        private int petAge;
        @ExcelColumn(headerName = "보호자 연락처")
        private String phoneNumber;
        @ExcelColumn(headerName = "비고")
        private String comment;
        @ExcelColumn(headerName = "상태")
        private ComparisonStatus status;
        @ExcelColumn(headerName = "유입 사이트")
        private String referSite;
        @ExcelColumn(headerName = "유입 사이트 유저 Id")
        private String referSiteUserId;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ComparisonDto {
        private Long comparisonId;
        private String createdAt;
        private String petType;
        private String petSpecies;
        private String petName;
        private int petAge;
        private String phoneNumber;
        private String comment;
        private String status;
        private String referSite;
        private String referUserId;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ComparisonPageDto {
        private List<ComparisonDto> content;
        private int currentPage; // 현재 페이지 번호
        private int pageSize; // 페이지 크기
        private int numberOfElement; // 현재 페이지의 element 개수
        private int totalElements; // 전체 element 개수
        private int totalPage; // 전체 페이지 개수
    }

}
