package FITPET.dev.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class ProposalRequest {
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProposalDto {
        @NotBlank(message = "이름 정보를 입력해주세요")
        private String name;
        @NotBlank(message = "이메일 정보를 입력해주세요")
        private String email;
        private String phoneNum;
        @NotBlank(message = "제휴 내용 정보를 입력해주세요")
        private String comment;
    }
}
