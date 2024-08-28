package FITPET.dev.repository;

import FITPET.dev.common.enums.Status;
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
            "AND (:status IS NULL OR p.status = :status)")
    Page<PetInfo> findAllByCreatedAtBetweenAndStatus(LocalDateTime startDate, LocalDateTime endDate, Status status, Pageable pageable);
}
