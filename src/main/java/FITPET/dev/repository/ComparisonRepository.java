package FITPET.dev.repository;

import static FITPET.dev.common.enums.PetType.DOG;

import FITPET.dev.common.enums.ComparisonStatus;
import FITPET.dev.common.enums.PetType;
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
            "WHERE c.deletedAt IS NULL " +
            "AND (:startDate IS NULL OR c.createdAt >= :startDate) " +
            "AND (:endDate IS NULL OR c.createdAt <= :endDate) " +
            "AND (:status IS NULL OR c.status = :status) " +
            "ORDER BY c.createdAt DESC ")
    Page<Comparison> findByCreatedAtBetweenAndStatus(@Param(value = "startDate") LocalDateTime startDate,
                                                  @Param(value = "endDate") LocalDateTime endDate,
                                                  @Param(value = "status") ComparisonStatus status,
                                                  Pageable pageable);

    @Query("SELECT c FROM Comparison c " +
            "WHERE c.deletedAt IS NULL " +
            "AND (:startDate IS NULL OR c.createdAt >= :startDate) " +
            "AND (:endDate IS NULL OR c.createdAt <= :endDate) " +
            "AND (:status IS NULL OR c.status = :status) " +
            "ORDER BY c.createdAt DESC ")
    List<Comparison> findByCreatedAtBetweenAndStatus(@Param(value = "startDate") LocalDateTime startDate,
                                                     @Param(value = "endDate") LocalDateTime endDate,
                                                     @Param(value = "status") ComparisonStatus status);


    @Query("SELECT c FROM Comparison c " +
            "WHERE c.deletedAt IS NULL " +
            "AND (:content IS NULL OR " +
            "(REPLACE(c.petInfo.phoneNum, '-', '') LIKE %:content% " +
            "OR c.petInfo.name LIKE %:content% " +
            "OR c.referSite.channel LIKE %:content% " +
            "OR c.petInfo.pet.petSpecies LIKE %:content% " +
            "OR c.comment LIKE %:content%)) " +
            "ORDER BY c.createdAt DESC")
    Page<Comparison> searchComparison(
            @Param("content") String content,
            Pageable pageable);

}
