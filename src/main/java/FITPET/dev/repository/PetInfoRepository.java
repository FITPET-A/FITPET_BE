package FITPET.dev.repository;

import FITPET.dev.entity.PetInfo;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PetInfoRepository extends JpaRepository<PetInfo, Long> {

}
