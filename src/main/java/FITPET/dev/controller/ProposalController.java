package FITPET.dev.controller;

import FITPET.dev.common.basecode.SuccessStatus;
import FITPET.dev.common.response.ApiResponse;
import FITPET.dev.dto.request.ProposalRequest;
import FITPET.dev.service.ProposalService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/proposal")
public class ProposalController {
    private final ProposalService proposalService;

    @PostMapping("")
    public ApiResponse postProposal(@RequestBody ProposalRequest.ProposalDto proposalDto){
        proposalService.postProposal(proposalDto);
        return ApiResponse.SuccessResponse(SuccessStatus.POST_PROPOSAL);
    }

}
