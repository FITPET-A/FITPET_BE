package FITPET.dev.repository;

import FITPET.dev.common.enums.ProposalStatus;
import FITPET.dev.entity.Proposal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ProposalRepository extends JpaRepository<Proposal, Long> {
    List<Proposal> findAllByOrderByCreatedAtDesc();

    @Query("SELECT p FROM Proposal p " +
            "WHERE (:startDate IS NULL OR p.createdAt >= :startDate) " +
            "AND (:endDate IS NULL OR p.createdAt <= :endDate) " +
            "AND (:status IS NULL OR p.status = :status) " +
            "ORDER BY p.createdAt DESC ")
    Page<Proposal> findByCreatedAtBetweenAndStatus(@Param(value = "startDate") LocalDateTime startDate,
                                                   @Param(value = "endDate") LocalDateTime endDate,
                                                   @Param(value = "status") ProposalStatus status, Pageable pageable);
}
