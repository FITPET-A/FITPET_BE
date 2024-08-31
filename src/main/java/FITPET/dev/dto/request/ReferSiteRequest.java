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
        @NotBlank(message = "채널을 입력해주세요")
        private String channel;
        @NotBlank(message = "채널 url을 입력해주세요")
        private String url;
        @NotBlank(message = "채널의 한글명을 입력해주세요")
        private String channelKor;
    }

}
