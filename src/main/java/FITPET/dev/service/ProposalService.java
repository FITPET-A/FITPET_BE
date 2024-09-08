package FITPET.dev.service;

import FITPET.dev.common.enums.ProposalStatus;
import FITPET.dev.common.status.ErrorStatus;
import FITPET.dev.common.exception.GeneralException;
import FITPET.dev.converter.ProposalConverter;
import FITPET.dev.dto.request.ProposalRequest;
import FITPET.dev.dto.response.ProposalResponse;
import FITPET.dev.entity.Proposal;
import FITPET.dev.repository.ProposalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class ProposalService {
    private final ProposalRepository proposalRepository;
    private final LocalDateTime minDateTime = LocalDateTime.of(2000, 1, 1, 0, 0);
    private final LocalDateTime maxDateTime = LocalDateTime.of(3999, 12, 31, 23, 59, 59);
    private static final Pattern PHONE_NUMBER_PATTERN = Pattern.compile("\\d{3}-\\d{3,4}-\\d{4}"); //  전화번호 정규식
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9+-\\_.]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$"); // 이메일 정규식


    /*
     * 제휴 제안 전송
     * @param proposalDto
     */
    @Transactional
    public void postProposal(ProposalRequest.ProposalDto proposalDto){

        // 패턴 및 유효성 검사
        validateEmail(proposalDto.getEmail());
        if (proposalDto.getPhoneNum() != null)
            validatePhoneNumber(proposalDto.getPhoneNum());

        // proposal 객체 생성 및 저장
        Proposal proposal = ProposalConverter.toProposal(proposalDto.getName(), proposalDto.getEmail(),
                proposalDto.getPhoneNum(), proposalDto.getComment());
        proposalRepository.save(proposal);
    }


    /*
     * 제휴문의 내역 조회
     * @param startDate
     * @param endDate
     * @param proposalStatus
     * @return
     */
    public ProposalResponse.ProposalPageDto getProposals(String startDate, String endDate, String proposalStatusStr, int page){

        ProposalStatus proposalStatus = proposalStatusStr.equals("all") ? null : ProposalStatus.valueOf(proposalStatusStr);

        // 날짜 형식 변경
        LocalDateTime start = (startDate != null) ? parseDate(startDate, " 00:00:00") : minDateTime;
        LocalDateTime end = (endDate != null) ? parseDate(endDate, " 23:59:59") : maxDateTime;

        int size = 20;
        Pageable pageable = PageRequest.of(page, size);

        // 제휴 제안 내역 조회
        Page<Proposal> proposalPage = getProposalListByStatusBetweenDate(start, end, proposalStatus, pageable);
        return ProposalConverter.toProposalPageDto(proposalPage);
    }

    /*
     * 제휴 제안 상태 변경
     * @param proposalId
     * @param proposalStatus
     * @return
     */
    @Transactional
    public ProposalResponse.ProposalDto patchProposalStatus(Long proposalId, ProposalStatus proposalStatus) {
        Proposal proposal = findProposalById(proposalId);

        // patch status
        proposal.updateStatus(proposalStatus);
        return ProposalConverter.toProposalDto(proposal);
    }


    /*
     * 제휴 제안 삭제
     * @param proposalId
     */
    @Transactional
    public void deleteProposal(Long proposalId) {
        Proposal proposal = findProposalById(proposalId);
        proposalRepository.delete(proposal);
    }

    private Page<Proposal> getProposalListByStatusBetweenDate(LocalDateTime start, LocalDateTime end, ProposalStatus proposalStatus, Pageable pageable) {
            // 특정 기간동안 생성된 status 정보가 일치하는 proposal List 최신순 정렬 후 반환
            return proposalRepository.findByCreatedAtBetweenAndStatus(start, end, proposalStatus, pageable);
    }


    private void validateEmail(String email) {
        if (!EMAIL_PATTERN.matcher(email).matches())
            throw new GeneralException(ErrorStatus.INVALID_EMAIL);
    }


    private void validatePhoneNumber(String phoneNum) {
        if (!PHONE_NUMBER_PATTERN.matcher(phoneNum).matches())
            throw new GeneralException(ErrorStatus.INVALID_PHONE_NUMBER);
    }

    private Proposal findProposalById(Long proposalId){
        return proposalRepository.findById(proposalId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.NOT_EXIST_PROPOSAL));
    }

    private LocalDateTime parseDate(String date, String timeSuffix) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        try {
            return LocalDateTime.parse(date + timeSuffix, formatter);
        } catch (DateTimeParseException e) {
            throw new GeneralException(ErrorStatus.INVALID_DATE_FORMAT);
        }
    }
}
