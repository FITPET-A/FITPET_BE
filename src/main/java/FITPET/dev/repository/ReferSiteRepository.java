package FITPET.dev.repository;

import FITPET.dev.entity.Insurance;
import FITPET.dev.entity.ReferSite;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReferSiteRepository extends JpaRepository<ReferSite, Long> {

    Optional<ReferSite> findByChannel(String channel);

    Optional<ReferSite> findByReferSiteId(Long ReferSiteId);

    @Query("SELECT r FROM ReferSite r WHERE r.deletedAt IS NULL")
    Page<ReferSite> findAllNotDeleted(Pageable pageable);

    @Query("SELECT r FROM ReferSite r WHERE r.deletedAt IS NULL AND r.referSiteId = :id")
    Optional<ReferSite> findByIdAndNotDeleted(@Param("id") Long id);

    @Query("SELECT r FROM ReferSite r WHERE r.deletedAt IS NULL AND " +
            "(:content IS NULL OR (r.channel LIKE %:content% OR r.url LIKE %:content% OR r.channelKor LIKE %:content%))")
    Page<ReferSite> searchReferSites(@Param("content") String content, Pageable pageable);
}
