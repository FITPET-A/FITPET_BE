package FITPET.dev.entity;

import FITPET.dev.common.enums.*;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Insurance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long insuranceId;

    @Column(nullable = false)
    private Company company;

    @Column(nullable = false)
    private int age;

    @Column(nullable = false, length = 10)
    private String dogBreedRank; // 견종등급

    @Column(nullable = false)
    private RenewalCycle renewalCycle; // 갱신주기

    @Column(nullable = false)
    private CoverageRatio coverageRatio; // 보상비율

    @Column(nullable = false)
    private Deductible deductible; // 자부담금

    @Column(nullable = false)
    private Compensation compensation; // 1일 보상

    @Column(nullable = true)
    private int premium; // 보험료
}
