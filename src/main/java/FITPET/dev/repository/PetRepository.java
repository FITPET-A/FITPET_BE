package FITPET.dev.repository;

import FITPET.dev.entity.Pet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PetRepository extends JpaRepository<Pet, Long> {
    Optional<Pet> findByDetailType(String detailType);
}
