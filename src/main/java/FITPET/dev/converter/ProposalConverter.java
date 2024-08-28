package FITPET.dev.converter;

import FITPET.dev.dto.response.ProposalResponse;
import FITPET.dev.entity.Proposal;

import java.util.List;

public class ProposalConverter {
    public static Proposal toProposal(String name, String email, String phoneNum, String comment){
        return Proposal.builder()
                .name(name)
                .email(email)
                .phoneNum(phoneNum)
                .comment(comment)
                .build();
    }

    public static ProposalResponse.ProposalDto toProposalDto(Proposal proposal){
        return ProposalResponse.ProposalDto.builder()
                .proposalId(proposal.getProposalId())
                .name(proposal.getName())
                .email(proposal.getEmail())
                .phoneNum(proposal.getPhoneNum())
                .comment(proposal.getComment())
                .build();
    }

    public static ProposalResponse.ProposalListDto toProposalListDto(List<Proposal> proposalList) {
        List<ProposalResponse.ProposalDto> proposalDtoList = proposalList.stream()
                .map(ProposalConverter::toProposalDto)
                .toList();

        return ProposalResponse.ProposalListDto.builder()
                .proposalDtoList(proposalDtoList)
                .build();
    }
}
