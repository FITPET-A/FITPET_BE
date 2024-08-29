package FITPET.dev.repository;

import FITPET.dev.common.enums.*;
import FITPET.dev.entity.Insurance;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface InsuranceRepository extends JpaRepository<Insurance, Long> {
    @Query("SELECT i FROM Insurance i " +
            "WHERE i.petType = :petType AND i.age = :age " +
            "AND i.renewalCycle = :renewalCycle AND i.coverageRatio = :coverageRatio " +
            "AND i.deductible = :deductible AND i.compensation = :compensation " +
            "AND i.deletedAt IS NULL "+
            "ORDER BY " +
            "i.dogBreedRank ASC, " +
            "i.renewalCycle DESC, " +
            "i.coverageRatio DESC, " +
            "i.age ASC")

    List<Insurance> findInsuranceList(@Param("petType") PetType petType,
                                      @Param("age") int age,
                                      @Param("renewalCycle") RenewalCycle renewalCycle,
                                      @Param(("coverageRatio")) CoverageRatio coverageRatio,
                                      @Param("deductible") Deductible deductible,
                                      @Param("compensation") Compensation compensation);


    @Query("SELECT i FROM Insurance i " +
            "WHERE i.company = :company AND i.deletedAt IS NULL " +
            "ORDER BY " +
            "i.dogBreedRank ASC, " +
            "i.renewalCycle DESC, " +
            "i.coverageRatio DESC, " +
            "i.age ASC")
    Page<Insurance> findByCompany(@Param(("company")) Company company,
                                  Pageable pageable);

    @Query("SELECT i FROM Insurance i WHERE i.deletedAt IS NULL " +
            "ORDER BY " +
            "i.dogBreedRank ASC, " +
            "i.renewalCycle DESC, " +
            "i.coverageRatio DESC, " +
            "i.age ASC")
    Page<Insurance> findAll(Pageable pageable);

    @Query("SELECT i FROM Insurance i WHERE i.deletedAt IS NULL " +
            "ORDER BY " +
            "i.dogBreedRank ASC, " +
            "i.renewalCycle DESC, " +
            "i.coverageRatio DESC, " +
            "i.age ASC")
    List<Insurance> findAll();

    Optional<Insurance> findByInsuranceId(Long insuranceId);


    @Query("SELECT i FROM Insurance i " +
            "WHERE i.petType = :petType AND i.deletedAt IS NULL " +
            "ORDER BY " +
            "i.dogBreedRank ASC, " +
            "i.renewalCycle DESC, " +
            "i.coverageRatio DESC, " +
            "i.age ASC")
    Page<Insurance> findByPetType(@Param(value = "petType") PetType petType,
                                  Pageable pageable);

    @Query("SELECT i FROM Insurance i " +
            "WHERE i.company = :company " +
            "AND i.petType = :petType " +
            "AND i.deletedAt IS NULL " +
            "ORDER BY " +
            "i.dogBreedRank ASC, " +
            "i.renewalCycle DESC, " +
            "i.coverageRatio DESC, " +
            "i.age ASC")
    Page<Insurance> findByCompanyAndPetType(@Param(value = "company") Company company,
                                            @Param(value = "petType") PetType petType,
                                            Pageable pageable);


    @Query("SELECT i FROM Insurance i WHERE i.deletedAt IS NOT NULL ORDER BY i.deletedAt DESC")
    Page<Insurance> findDeleted(Pageable pageable);


}

