package FITPET.dev.dto.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class RefSiteResponse {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RefSiteDto {
        private Long refSiteId;
        private String channel;
        private String url;
        private String channelKor;
    }

}
