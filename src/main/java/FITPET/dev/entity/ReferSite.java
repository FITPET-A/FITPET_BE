package FITPET.dev.entity;

import FITPET.dev.common.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReferSite extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long referSiteId;

    @Column(nullable = true, length = 30)
    private String channel;

    @Column(nullable = false, length = 100)
    private String url;

    @Column(nullable = false, length = 30)
    private String channelKor;

    @Column(nullable = true)
    private LocalDateTime deletedAt;







}
