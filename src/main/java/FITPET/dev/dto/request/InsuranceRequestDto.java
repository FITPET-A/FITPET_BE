package FITPET.dev.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InsuranceRequestDto {
    private String company; // 메리츠, DB, 현대, KB, 삼성
    private String petType; // dog, cat
    private int age; // 나이
    private String dogBreedRank; // 견종등급
    private String renewalCycle; // 갱신주기
    private String coverageRatio; // 보상비율
    private String deductible; // 자부담금
    private String compensation; // 1일 보상
    private int premium; // 보험료
}

