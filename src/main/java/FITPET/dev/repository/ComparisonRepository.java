package FITPET.dev.repository;

import FITPET.dev.common.enums.ComparisonStatus;
import FITPET.dev.entity.Comparison;
import FITPET.dev.entity.PetInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ComparisonRepository extends JpaRepository<Comparison, Long> {

    @Query("SELECT c FROM Comparison c " +
            "WHERE (:startDate IS NULL OR c.createdAt >= :startDate) " +
            "AND (:endDate IS NULL OR c.createdAt <= :endDate) " +
            "AND (:status IS NULL OR c.status = :status) " +
            "ORDER BY c.createdAt DESC ")
    Page<Comparison> findByCreatedAtBetweenAndStatus(@Param(value = "startDate") LocalDateTime startDate,
                                                  @Param(value = "endDate") LocalDateTime endDate,
                                                  @Param(value = "status") ComparisonStatus status,
                                                  Pageable pageable);

    @Query("SELECT c FROM Comparison c " +
            "WHERE (:startDate IS NULL OR c.createdAt >= :startDate) " +
            "AND (:endDate IS NULL OR c.createdAt <= :endDate) " +
            "AND (:status IS NULL OR c.status = :status) " +
            "ORDER BY c.createdAt DESC ")
    List<Comparison> findByCreatedAtBetweenAndStatus(@Param(value = "startDate") LocalDateTime startDate,
                                                     @Param(value = "endDate") LocalDateTime endDate,
                                                     @Param(value = "status") ComparisonStatus status);


    @Query("SELECT c FROM Comparison c " +
            "WHERE REPLACE(c.petInfo.phoneNum, '-', '') LIKE %?1% " +
            "OR c.petInfo.name LIKE %?1%")
    Page<Comparison> findAllByPhoneNumOrPetName(String content, Pageable pageable);
}
