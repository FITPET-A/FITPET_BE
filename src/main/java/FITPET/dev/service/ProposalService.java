package FITPET.dev.service;

import FITPET.dev.common.basecode.ErrorStatus;
import FITPET.dev.common.exception.GeneralException;
import FITPET.dev.converter.InquiryConverter;
import FITPET.dev.converter.ProposalConverter;
import FITPET.dev.dto.request.ProposalRequest;
import FITPET.dev.entity.Proposal;
import FITPET.dev.repository.ProposalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

    // 이메일 유효성 검사
    private void validateEmail(String email) {
        if (!EMAIL_PATTERN.matcher(email).matches())
            throw new GeneralException(ErrorStatus.INVALID_EMAIL);
    }

    // 전화번호 유효성 검사
    private void validatePhoneNumber(String phoneNum) {
        if (!PHONE_NUMBER_PATTERN.matcher(phoneNum).matches())
            throw new GeneralException(ErrorStatus.INVALID_PHONE_NUMBER);
    }
}
