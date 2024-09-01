package FITPET.dev.controller;

import FITPET.dev.common.status.SuccessStatus;
import FITPET.dev.common.response.ApiResponse;
import FITPET.dev.dto.request.ProposalRequest;
import FITPET.dev.service.ProposalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "제휴 제안 API")
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1")
@CrossOrigin(origins = "http://localhost:3000")
public class ProposalController {
    private final ProposalService proposalService;

    @PostMapping("/proposal")
    @Operation(summary = "제휴 제안 전송 API", description = "이름, 이메일, 전화번호, 내용을 Parameter으로 받아 제휴 제안서를 전송")
    public ApiResponse postProposal(@RequestBody ProposalRequest.ProposalDto proposalDto){
        proposalService.postProposal(proposalDto);
        return ApiResponse.SuccessResponse(SuccessStatus.POST_PROPOSAL);
    }

    /*
     * admin
     */
    @GetMapping("/admin/proposal")
    @Operation(summary = "제휴 제안 내역 전체 조회 API", description = "제휴 제안 내역 전체 조회")
    public ApiResponse getProposals(){
        return ApiResponse.SuccessResponse(SuccessStatus.GET_PROPOSAL, proposalService.getProposals());
    }

}
