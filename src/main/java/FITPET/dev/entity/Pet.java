package FITPET.dev.entity;

import FITPET.dev.common.enums.PetType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PetType petType;

    @Column(nullable = false, length = 30)
    private String detailType;
}
