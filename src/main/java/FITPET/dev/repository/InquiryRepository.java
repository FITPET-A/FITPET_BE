package FITPET.dev.repository;

import FITPET.dev.common.enums.InquiryStatus;
import FITPET.dev.entity.Inquiry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface InquiryRepository extends JpaRepository<Inquiry, Long> {

    @Query("SELECT i FROM Inquiry i " +
            "WHERE (:startDate IS NULL OR i.createdAt >= :startDate) " +
            "AND (:endDate IS NULL OR i.createdAt <= :endDate) " +
            "ORDER BY i.createdAt DESC ")
    List<Inquiry> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);


    @Query("SELECT i FROM Inquiry i " +
            "WHERE (:startDate IS NULL OR i.createdAt >= :startDate) " +
            "AND (:endDate IS NULL OR i.createdAt <= :endDate) " +
            "AND (:status IS NULL OR i.status = :status) " +
            "ORDER BY i.createdAt DESC ")
    List<Inquiry> findByCreatedAtBetweenAndStatus(LocalDateTime startDate, LocalDateTime endDate, InquiryStatus status);
}
