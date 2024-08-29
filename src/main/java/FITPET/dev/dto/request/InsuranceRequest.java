package FITPET.dev.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InsuranceRequest {
    @NotBlank(message = "회사 정보가 필요합니다.")
    private String company; // 메리츠, DB, 현대, KB, 삼성
    @NotBlank(message = "펫 종류 정보가 필요합니다.")
    private String petType; // dog, cat
    @NotBlank(message = "나이 정보가 필요합니다.")
    private int age; // 나이
    private String dogBreedRank; // 견종등급
    @NotBlank(message = "갱신주기 정보가 필요합니다.")
    private String renewalCycle; // 갱신주기
    @NotBlank(message = "보상비율 정보가 필요합니다.")
    private String coverageRatio; // 보상비율
    @NotBlank(message = "자부담금 정보가 필요합니다.")
    private String deductible; // 자부담금
    @NotBlank(message = "1일 보상 정보가 필요합니다.")
    private String compensation; // 1일 보상
    @NotBlank(message = "보험료 정보가 필요합니다.")
    private int premium; // 보험료
}

