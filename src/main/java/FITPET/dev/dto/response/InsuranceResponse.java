package FITPET.dev.dto.response;


import FITPET.dev.common.enums.*;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

public class InsuranceResponse {
    @Data @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InsuranceListDto {
        private List<InsuranceDto> insuranceDtoList;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InsuranceDto {
        private String company;
        private int premium;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InsuranceDetailPageDto {
        private List<InsuranceDetailDto> content;
        private int currentPage; //현재 페이지 번호
        private int pageSize; //페이지 크기
        private int totalNumber; //전체 content 개수
        private int totalPage; //전체 페이지 개수
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InsuranceDetailDto {
        private PetType petType;
        private int age;
        private String dogBreedRank; // 견종등급
        private String renewalCycle; // 갱신주기
        private String coverageRatio; // 보상비율
        private String deductible; // 자부담금
        private String compensation; // 1일 보상
        private int premium;
    }

}
