package FITPET.dev.entity;


import FITPET.dev.common.base.BaseEntity;
import FITPET.dev.common.enums.ComparisonStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Comparison extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long comparisonId;

    @OneToOne
    @JoinColumn(name = "pet_info_id", nullable = true)
    private PetInfo petInfo;

    @Column(nullable = true)
    private String referSite;

    @Column(nullable = true)
    private String referSiteUserId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ComparisonStatus status;

    @Column(nullable = true, length = 500)
    private String comment;

    public void updateStatus(ComparisonStatus status){
        this.status = status;
    }

}
