package FITPET.dev.dto.response;

import FITPET.dev.common.enums.PetType;
import FITPET.dev.dto.response.InsuranceResponse.InsuranceDetailDto;
import FITPET.dev.dto.response.InsuranceResponse.InsuranceDto;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class ReferSiteResponse {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReferSiteDto {
        private Long refSiteId;
        private String channel;
        private String url;
        private String channelKor;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReferSiteListDto {
        private List<ReferSiteDto> ReferSiteDtoList;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReferSiteDetailDto {
        private Long refSiteId;
        private String channel;
        private String url;
        private String channelKor;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReferSitePageDto {
        private List<ReferSiteDto> content;
        private int currentPage; // 현재 페이지 번호
        private int pageSize; // 페이지 크기
        private int totalNumber; // 전체 content 개수
        private int totalPage; // 전체 페이지 개수
    }
}
