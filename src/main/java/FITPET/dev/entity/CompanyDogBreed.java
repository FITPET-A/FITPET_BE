package FITPET.dev.entity;

import FITPET.dev.common.enums.Company;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompanyDogBreed {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long companyDogBreedId;

    @ManyToOne
    @JoinColumn(name = "pet_id", nullable = true)
    private Pet pet;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Company company;

    @Column(nullable = true, length = 30)
    private String dogBreed; // 견종분류

    @Column(nullable = true, length = 10)
    private String dogBreedRank; // 견종등급

}
