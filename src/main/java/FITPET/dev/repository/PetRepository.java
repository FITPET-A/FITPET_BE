package FITPET.dev.repository;

import FITPET.dev.common.enums.PetType;
import FITPET.dev.entity.Pet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PetRepository extends JpaRepository<Pet, Long> {
    List<Pet> findByPetTypeAndDetailTypeContaining(PetType petType, String detailType);
}
