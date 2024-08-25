package FITPET.dev.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DogBreedDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long dogBreedDetailId;

    @OneToOne
    @JoinColumn(name = "pet_id", nullable = true)
    private Pet pet;

    @Column(nullable = false)
    private boolean isFierceDog;

    @Column(nullable = true, length = 30)
    private String liabilityCap; // 배상책임인수제한

    @Column(nullable = false, length = 10)
    private String isMajorDogBreed; // 주요 견종 여부

}
