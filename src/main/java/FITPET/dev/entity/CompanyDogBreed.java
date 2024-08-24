package FITPET.dev.entity;

import FITPET.dev.common.enums.Company;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Entity
@Getter
@Builder
@RequiredArgsConstructor
public class CompanyDogBreed {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long companyDogBreedId;

    @Column(nullable = false)
    private Company company;

    @Column(nullable = true)
    private String dogBreed;

    @Column(nullable = false)
    private String dogBreedRank;

}
