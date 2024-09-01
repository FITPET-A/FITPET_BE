package FITPET.dev.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class ReferSiteRequest {
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReferSiteDto {
        private String channel;
        private String url;
        private String channelKor;
    }

}
