package FITPET.dev.entity;

import FITPET.dev.common.base.BaseEntity;
import FITPET.dev.common.enums.*;
import com.fasterxml.jackson.databind.ser.Serializers.Base;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Insurance extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long insuranceId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Company company;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PetType petType;

    @Column(nullable = false)
    private int age;

    @Column(nullable = true, length = 20)
    private String dogBreedRank; // 견종등급

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RenewalCycle renewalCycle; // 갱신주기

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CoverageRatio coverageRatio; // 보상비율

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Deductible deductible; // 자부담금

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Compensation compensation; // 1일 보상

    @Column(nullable = true)
    private int premium; // 보험료

    @Column(nullable = true)
    private LocalDateTime deletedAt; // 삭제된 날짜


    @Override
    public String toString() {
        return "company="+company
                +"/petType="+petType
                +"/age="+age
                +"/dogBreedRank="+dogBreedRank
                +"/renewalCycle="+renewalCycle
                +"/coverageRatio="+coverageRatio
                +"/deductible="+deductible
                +"/compensation="+compensation
                +"/premium="+premium;
    }

    public void updatepremium(int premium){
        this.premium = premium;
    }

    public void setDeletedAt(){
        this.deletedAt = LocalDateTime.now();
    }
}
