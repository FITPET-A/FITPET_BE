package FITPET.dev.entity;

import FITPET.dev.common.enums.PetType;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Pet {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long petId;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "pet")
    private DogBreedDetail dogBreedDetail;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "pet")
    private List<CompanyDogBreed> companyDogBreedList;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PetType petType;

    @Column(nullable = false, length = 30)
    private String petSpecies;
}
