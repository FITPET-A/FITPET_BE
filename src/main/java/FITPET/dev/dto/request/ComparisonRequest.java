package FITPET.dev.dto.request;

import FITPET.dev.common.enums.PetType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ComparisonRequest {
    private String petName;
    private PetType petType;
    private String petSpecies;
    private int petAge;
    private String phoneNumber;
    private String comment;
    private String referSite;
    private String referUserId;
}
