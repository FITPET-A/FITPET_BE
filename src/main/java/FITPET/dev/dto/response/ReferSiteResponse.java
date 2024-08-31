package FITPET.dev.dto.response;

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

}
