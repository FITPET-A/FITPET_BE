package FITPET.dev.dto.response;


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
}
