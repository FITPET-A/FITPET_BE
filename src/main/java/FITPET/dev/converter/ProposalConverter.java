package FITPET.dev.converter;

import FITPET.dev.common.enums.ProposalStatus;
import FITPET.dev.dto.response.ProposalResponse;
import FITPET.dev.entity.Proposal;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;

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
                .status(proposal.getStatus().toString())
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

    public static ProposalResponse.ProposalPageDto toProposalPageDto(Page<Proposal> page) {
        List<ProposalResponse.ProposalDto> prosposalDtoList = page.getContent().stream()
                .map(ProposalConverter::toProposalDto)
                .collect(Collectors.toList());

        return ProposalResponse.ProposalPageDto.builder()
                .content(prosposalDtoList)
                .currentPage(page.getNumber() + 1)
                .pageSize(page.getSize())
                .numberOfElement(page.getNumberOfElements())
                .totalElements((int) page.getTotalElements())
                .totalPage(page.getTotalPages())
                .build();
    }
}
