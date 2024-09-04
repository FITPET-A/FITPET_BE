package FITPET.dev.controller;

import FITPET.dev.common.enums.InquiryStatus;
import FITPET.dev.common.enums.ProposalStatus;
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
    @Operation(summary = "제휴 제안 내역 조회 API", description = "시작 날짜, 마지막 날짜, 상태값을 Parameter으로 받아 특정 기간 동안 생성된 제휴 제안 내역 조회")
    public ApiResponse getProposals(
            @RequestParam(name = "startDate", required = false) String startDate,
            @RequestParam(name = "endDate", required = false) String endDate,
            @RequestParam(name = "status", required = false) ProposalStatus proposalStatus
    ){
        return ApiResponse.SuccessResponse(SuccessStatus.GET_PROPOSAL, proposalService.getProposals(startDate, endDate, proposalStatus));
    }

    @PatchMapping("/admin/proposal/status/{proposalId}")
    @Operation(summary = "제휴 제안 상태 변경 API", description = "특정 제휴 제안 정보의 상태값을 변경")
    public ApiResponse patchProposalStatus(
            @PathVariable(value = "proposalId") Long proposalId,
            @RequestParam(name = "status")ProposalStatus proposalStatus
            ) {
        return ApiResponse.SuccessResponse(SuccessStatus.PATCH_PROPOSAL_STATUS, proposalService.patchProposalStatus(proposalId, proposalStatus));
    }

    @DeleteMapping("/admin/proposal/{proposalId}")
    @Operation(summary = "제휴 제안 삭제 API", description = "특정 제휴 제안 정보를 삭제")
    public ApiResponse deleteProposal(@PathVariable(value = "proposalId") Long proposalId){
        proposalService.deleteProposal(proposalId);
        return ApiResponse.SuccessResponse(SuccessStatus.DELETE_PROPOSAL);
    }
}
