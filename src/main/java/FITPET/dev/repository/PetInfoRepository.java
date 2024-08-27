package FITPET.dev.repository;

import FITPET.dev.entity.PetInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PetInfoRepository extends JpaRepository<PetInfo, Long> {

    @Query("SELECT p FROM PetInfo p ORDER BY p.createdAt DESC")
    List<PetInfo> findAll();



}
