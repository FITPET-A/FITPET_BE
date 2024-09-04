package FITPET.dev.converter;

import FITPET.dev.common.enums.ProposalStatus;
import FITPET.dev.dto.response.ProposalResponse;
import FITPET.dev.entity.Proposal;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ProposalConverter {
    public static String formatDateTime(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return dateTime.format(formatter);
    }
    public static Proposal toProposal(String name, String email, String phoneNum, String comment){
        return Proposal.builder()
                .name(name)
                .email(email)
                .phoneNum(phoneNum)
                .comment(comment)
                .status(ProposalStatus.PENDING)
                .build();
    }

    public static ProposalResponse.ProposalDto toProposalDto(Proposal proposal){
        return ProposalResponse.ProposalDto.builder()
                .proposalId(proposal.getProposalId())
                .createdAt(formatDateTime(proposal.getCreatedAt()))
                .name(proposal.getName())
                .email(proposal.getEmail())
                .phoneNum(proposal.getPhoneNum())
                .comment(proposal.getComment())
                .status(proposal.getStatus().getLabel())
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
