package FITPET.dev.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Entity
@Getter
@Builder
@RequiredArgsConstructor
public class DogBreedDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long dogBreedDetailId;

    @Column(nullable = false)
    private boolean isFierceDog;

    @Column(nullable = true)
    private String liabilityCap; // 배상책임인수제한

    @Column(nullable = false)
    private String isMajorDogBreed; // 주요 견종 여부

}
