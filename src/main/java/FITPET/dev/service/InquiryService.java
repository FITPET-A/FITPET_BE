package FITPET.dev.service;

import FITPET.dev.common.enums.InquiryStatus;
import FITPET.dev.common.status.ErrorStatus;
import FITPET.dev.common.exception.GeneralException;
import FITPET.dev.converter.InquiryConverter;
import FITPET.dev.converter.ProposalConverter;
import FITPET.dev.dto.request.InquiryRequest;
import FITPET.dev.dto.response.InquiryResponse;
import FITPET.dev.entity.Inquiry;
import FITPET.dev.entity.Proposal;
import FITPET.dev.repository.InquiryRepository;
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
public class InquiryService {
    private final InquiryRepository inquiryRepository;

    // 최소, 최대 조회 기간
    private final LocalDateTime minDateTime = LocalDateTime.of(2000, 1, 1, 0, 0);
    private final LocalDateTime maxDateTime = LocalDateTime.of(3999, 12, 31, 23, 59, 59);
    private static final Pattern PHONE_NUMBER_PATTERN = Pattern.compile("\\d{3}-\\d{3,4}-\\d{4}"); //  전화번호 정규식
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9+-\\_.]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$"); // 이메일 정규식


    /*
     * 1:1 문의 전송
     * @param inquiryDto
     */
    @Transactional
    public InquiryResponse.InquiryDto postInquiry(InquiryRequest.InquiryDto inquiryDto){

        // 패턴 및 유효성 검사
        validateEmail(inquiryDto.getEmail());
        if (inquiryDto.getPhoneNum() != null)
            validatePhoneNumber(inquiryDto.getPhoneNum());

        // inquiry 객체 생성 및 저장
        Inquiry inquiry = InquiryConverter.toInquiry(inquiryDto.getName(), inquiryDto.getEmail(),
                inquiryDto.getPhoneNum(), inquiryDto.getComment());
        return InquiryConverter.toInquiryDto(inquiryRepository.save(inquiry));
    }


    /*
     * 1:1 문의 내역 조회
     * @param startDate
     * @param endDate
     * @param inquiryStatus
     * @return
     */
    public InquiryResponse.InquiryPageDto getInquiries(String startDate, String endDate, InquiryStatus inquiryStatus, int page){

        // 날짜 형식 변경
        LocalDateTime start = (startDate != null) ? parseDate(startDate, " 00:00:00") : minDateTime;
        LocalDateTime end = (endDate != null) ? parseDate(endDate, " 23:59:59") : maxDateTime;

        int size = 20;
        Pageable pageable = PageRequest.of(page, size);

        Page<Inquiry> inquiryPage = getInquiryListByStatusBetweenDate(start, end, inquiryStatus, pageable);
        return InquiryConverter.toInquiryPageDto(inquiryPage);


    }



    /*
     * 1:1 문의 상태 변경
     * @param inquiryId
     * @param inquiryStatus
     * @return
     */
    @Transactional
    public InquiryResponse.InquiryDto patchInquiryStatus(Long inquiryId, InquiryStatus inquiryStatus) {

        // 1:1 문의 단일 조회
        Inquiry inquiry = findInquiryById(inquiryId);

        // validate status
        InquiryStatus currentInquiryStatus = inquiry.getStatus();
        if (currentInquiryStatus.getIndex() > inquiryStatus.getIndex())
            throw new GeneralException(ErrorStatus.INVALID_PATCH_PERIOR_STATUS);

        // patch status
        inquiry.updateStatus(inquiryStatus);
        return InquiryConverter.toInquiryDto(inquiry);
    }


    /*
     * 1:1 문의 삭제
     * @param inquiryId
     */
    @Transactional
    public void deleteInquiry(Long inquiryId) {
        Inquiry inquiry = findInquiryById(inquiryId);
        inquiryRepository.delete(inquiry);
    }


    private Page<Inquiry> getInquiryListByStatusBetweenDate(LocalDateTime start, LocalDateTime end, InquiryStatus inquiryStatus, Pageable pageable) {

        if (inquiryStatus == null){
            // 특정 기간동안 생성된 inquiry List 최신순 정렬 후 반환
            return inquiryRepository.findByCreatedAtBetween(start, end, pageable);
        } else {
            // 특정 기간동안 생성된 status 정보가 일치하는 inquiry List 최신순 정렬 후 반환
            return inquiryRepository.findByCreatedAtBetweenAndStatus(start, end, inquiryStatus, pageable);
        }
    }

    private Inquiry findInquiryById(Long inquiryId) {
        return inquiryRepository.findById(inquiryId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.NOT_EXIST_INQUIRY));
    }

    private void validateEmail(String email) {
        if (!EMAIL_PATTERN.matcher(email).matches()){
            throw new GeneralException(ErrorStatus.INVALID_EMAIL);
        }
    }

    private void validatePhoneNumber(String phoneNum) {
        if (!PHONE_NUMBER_PATTERN.matcher(phoneNum).matches()) {
            throw new GeneralException(ErrorStatus.INVALID_PHONE_NUMBER);
        }
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
