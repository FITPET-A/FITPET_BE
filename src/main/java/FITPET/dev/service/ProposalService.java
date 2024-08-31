package FITPET.dev.service;

import FITPET.dev.common.status.ErrorStatus;
import FITPET.dev.common.exception.GeneralException;
import FITPET.dev.converter.ProposalConverter;
import FITPET.dev.dto.request.ProposalRequest;
import FITPET.dev.dto.response.ProposalResponse;
import FITPET.dev.entity.Proposal;
import FITPET.dev.repository.ProposalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class ProposalService {
    private final ProposalRepository proposalRepository;
    private static final Pattern PHONE_NUMBER_PATTERN = Pattern.compile("\\d{3}-\\d{3,4}-\\d{4}"); //  전화번호 정규식
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9+-\\_.]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$"); // 이메일 정규식


    /*
     * 제휴 제안 전송
     * @param proposalDto
     */
    public void postProposal(ProposalRequest.ProposalDto proposalDto){

        // 패턴 및 유효성 검사
        validateEmail(proposalDto.getEmail());
        validatePhoneNumber(proposalDto.getPhoneNum());

        // proposal 객체 생성 및 저장
        Proposal proposal = ProposalConverter.toProposal(proposalDto.getName(), proposalDto.getEmail(),
                proposalDto.getPhoneNum(), proposalDto.getComment());
        proposalRepository.save(proposal);
    }


    /*
     * 제휴문의 내역 전체 조회
     * @return
     */
    public ProposalResponse.ProposalListDto getProposals(){

        // 전체 제휴문의 내역 조회
        List<Proposal> proposalList = proposalRepository.findAllByOrderByCreatedAtDesc();

        return ProposalConverter.toProposalListDto(proposalList);
    }


    private void validateEmail(String email) {
        if (!EMAIL_PATTERN.matcher(email).matches())
            throw new GeneralException(ErrorStatus.INVALID_EMAIL);
    }


    private void validatePhoneNumber(String phoneNum) {
        if (!PHONE_NUMBER_PATTERN.matcher(phoneNum).matches())
            throw new GeneralException(ErrorStatus.INVALID_PHONE_NUMBER);
    }
}
