package FITPET.dev.repository;

import FITPET.dev.common.enums.*;
import FITPET.dev.entity.Insurance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface InsuranceRepository extends JpaRepository<Insurance, Long> {
    @Query("SELECT i FROM Insurance i " +
            "WHERE i.petType = :petType AND i.age = :age " +
            "AND i.renewalCycle = :renewalCycle AND i.coverageRatio = :coverageRatio " +
            "AND i.deductible = :deductible AND i.compensation = :compensation")
    List<Insurance> findInsuranceList(@Param("petType") PetType petType,
                                      @Param("age") int age,
                                      @Param("renewalCycle") RenewalCycle renewalCycle,
                                      @Param("coverageRatio") CoverageRatio coverageRatio,
                                      @Param("deductible") Deductible deductible,
                                      @Param("compensation") Compensation compensation);
}

