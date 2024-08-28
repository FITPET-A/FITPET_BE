package FITPET.dev.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

public class ProposalResponse {
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProposalDto {
        private Long proposalId;
        private String createdAt;
        private String name;
        private String email;
        private String phoneNum;
        private String comment;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProposalListDto {
        private List<ProposalDto> proposalDtoList;
    }
}
