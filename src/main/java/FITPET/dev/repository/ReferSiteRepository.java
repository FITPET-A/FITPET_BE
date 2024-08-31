package FITPET.dev.repository;

import FITPET.dev.entity.Insurance;
import FITPET.dev.entity.ReferSite;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReferSiteRepository extends JpaRepository<ReferSite, Long> {

    Optional<ReferSite> findByChannel(String channel);

    Optional<ReferSite> findById(Long referSiteId);

}
