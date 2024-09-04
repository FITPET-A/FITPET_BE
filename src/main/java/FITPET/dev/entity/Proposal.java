package FITPET.dev.entity;

import FITPET.dev.common.base.BaseEntity;
import FITPET.dev.common.enums.InquiryStatus;
import FITPET.dev.common.enums.ProposalStatus;
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
public class Proposal extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long proposalId;

    @Column(nullable = false, length = 10)
    private String name;

    @Column(nullable = false, length = 30)
    private String email;

    @Column(nullable = true, length = 30)
    private String phoneNum;

    @Column(nullable = false, length = 500)
    private String comment;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProposalStatus status;

    public void updateStatus(ProposalStatus proposalStatus){
        this.status = proposalStatus;
    }
}
