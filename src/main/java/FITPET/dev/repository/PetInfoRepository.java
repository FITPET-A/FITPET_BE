package FITPET.dev.repository;

import FITPET.dev.common.enums.PetInfoStatus;
import FITPET.dev.entity.PetInfo;
import java.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PetInfoRepository extends JpaRepository<PetInfo, Long> {

    @Query("SELECT p FROM PetInfo p " +
            "WHERE (:startDate IS NULL OR p.createdAt >= :startDate) " +
            "AND (:endDate IS NULL OR p.createdAt <= :endDate) " +
            "AND (:status IS NULL OR p.petInfoStatus = :status) " +
            "ORDER BY p.createdAt DESC ")
    Page<PetInfo> findByCreatedAtBetweenAndStatus(LocalDateTime startDate, LocalDateTime endDate, PetInfoStatus petInfoStatus, Pageable pageable);

    @Query("SELECT p FROM PetInfo p " +
            "WHERE (:startDate IS NULL OR p.createdAt >= :startDate) " +
            "AND (:endDate IS NULL OR p.createdAt <= :endDate) " +
            "AND (:status IS NULL OR p.petInfoStatus = :status) " +
            "ORDER BY p.createdAt DESC ")
    List<PetInfo> findByCreatedAtBetweenAndStatus(LocalDateTime startDate, LocalDateTime endDate, PetInfoStatus petInfoStatus);

    @Query("SELECT p FROM PetInfo p WHERE REPLACE(p.phoneNum, '-', '') LIKE %?1% OR p.name LIKE %?1%")
    Page<PetInfo> findAllByPhoneNumOrPetName(String content, Pageable pageable);

}
