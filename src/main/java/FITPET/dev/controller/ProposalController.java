package FITPET.dev.controller;

import FITPET.dev.common.status.SuccessStatus;
import FITPET.dev.common.response.ApiResponse;
import FITPET.dev.dto.request.ProposalRequest;
import FITPET.dev.service.ProposalService;
import io.swagger.v3.oas.annotations.Operation;
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
    @Operation(summary = "제휴 제안 전송 API", description = "이름, 이메일, 전화번호, 내용을 Parameter으로 받아 제휴 제안서를 전송")
    public ApiResponse postProposal(@RequestBody ProposalRequest.ProposalDto proposalDto){
        proposalService.postProposal(proposalDto);
        return ApiResponse.SuccessResponse(SuccessStatus.POST_PROPOSAL);
    }

}
