package FITPET.dev.entity;

import FITPET.dev.common.enums.PetType;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Entity
@Getter
@Builder
@RequiredArgsConstructor
public class Pet {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long petId;

    @OneToOne
    @JoinColumn(name = "dogBreedDetailId", nullable = true)
    private DogBreedDetail dogBreedDetailId;

    @OneToOne
    @JoinColumn(name = "companyDogBreedId", nullable = true)
    private CompanyDogBreed companyDogBreed;

    @Column(nullable = false)
    private PetType petType;

    @Column(nullable = false)
    private String detailType;
}
