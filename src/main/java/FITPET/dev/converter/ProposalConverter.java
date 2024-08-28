package FITPET.dev.converter;

import FITPET.dev.entity.Proposal;

public class ProposalConverter {
    public static Proposal toProposal(String name, String email, String phoneNum, String comment){
        return Proposal.builder()
                .name(name)
                .email(email)
                .phoneNum(phoneNum)
                .comment(comment)
                .build();
    }
}
