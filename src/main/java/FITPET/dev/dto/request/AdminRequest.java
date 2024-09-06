package FITPET.dev.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class AdminRequest {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SignUpDto {
        @NotBlank
        private String id;
        @NotBlank
        private String password;
        @NotBlank
        private String adminName;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SignInDto {
        @NotBlank
        private String id;

        @NotBlank
        private String password;
    }
}
