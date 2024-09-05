package FITPET.dev.dto.request;

import FITPET.dev.common.enums.PetType;
import FITPET.dev.entity.ReferSite;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ComparisonRequest {
    @NotBlank
    private String petName;
    @NotBlank
    private PetType petType;
    @NotBlank
    private String petSpecies;
    @NotBlank
    private int petAge;
    @NotBlank
    private String phoneNumber;
    private String comment;
    private String referSite;
    private String referUserId;

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ComparisonDto {
        @NotBlank
        private List<Long> comparisonIds;
    }
}
