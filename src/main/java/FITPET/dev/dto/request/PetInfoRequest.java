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
public class PetInfoRequest {
    private String name;
    private int age;
    private String phoneNum;
    private PetType petType;
    private String detailType;
    private String comment;
}
