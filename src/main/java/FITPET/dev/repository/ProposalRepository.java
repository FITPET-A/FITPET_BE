package FITPET.dev.repository;

import FITPET.dev.entity.Proposal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProposalRepository extends JpaRepository<Proposal, Long> {
    List<Proposal> findAllByOrderByCreatedAtAsc();
}
