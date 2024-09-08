package FITPET.dev.repository;

import FITPET.dev.common.enums.InquiryStatus;
import FITPET.dev.entity.Inquiry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface InquiryRepository extends JpaRepository<Inquiry, Long> {

    @Query("SELECT i FROM Inquiry i " +
            "WHERE (:startDate IS NULL OR i.createdAt >= :startDate) " +
            "AND (:endDate IS NULL OR i.createdAt <= :endDate) " +
            "AND (:status IS NULL OR i.status = :status) " +
            "ORDER BY i.createdAt DESC ")
    Page<Inquiry> findByCreatedAtBetweenAndStatus(@Param(value = "startDate") LocalDateTime startDate,
                                                  @Param(value = "endDate") LocalDateTime endDate,
                                                  @Param(value = "status") InquiryStatus status,
                                                  Pageable pageable);
}
