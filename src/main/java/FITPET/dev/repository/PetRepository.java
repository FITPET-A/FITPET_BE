package FITPET.dev.repository;

import FITPET.dev.common.enums.PetType;
import FITPET.dev.entity.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

import java.util.Optional;

public interface PetRepository extends JpaRepository<Pet, Long> {

    @Query("SELECT p FROM Pet p WHERE p.petType = :petType AND p.petSpecies LIKE %:petSpecies% ORDER BY p.petSpecies ASC")
    List<Pet> findPetListContainingDetailType(@Param("petType") PetType petType, @Param("petSpecies") String petSpecies);

    Optional<Pet> findByPetSpecies(String petSpecies);
}
