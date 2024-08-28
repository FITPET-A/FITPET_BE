package FITPET.dev.repository;

import FITPET.dev.entity.InsuranceHistory;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InsuranceHistoryRepository extends JpaRepository<InsuranceHistory, Long> {

    List<InsuranceHistory> findByInsurance_InsuranceIdOrderByCreatedAtDesc(Long insuranceId);


}
