package FITPET.dev.service;

import FITPET.dev.common.status.ErrorStatus;
import FITPET.dev.common.exception.GeneralException;
import FITPET.dev.converter.InquiryConverter;
import FITPET.dev.dto.request.InquiryRequest;
import FITPET.dev.dto.response.InquiryResponse;
import FITPET.dev.entity.Inquiry;
import FITPET.dev.repository.InquiryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class InquiryService {
    private final InquiryRepository inquiryRepository;
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
        validatePhoneNumber(inquiryDto.getPhoneNum());

        // inquiry 객체 생성 및 저장
        Inquiry inquiry = InquiryConverter.toInquiry(inquiryDto.getName(), inquiryDto.getEmail(),
                inquiryDto.getPhoneNum(), inquiryDto.getComment());
        return InquiryConverter.toInquiryDto(inquiryRepository.save(inquiry));
    }

    // 이메일 유효성 검사
    private void validateEmail(String email) {
        if (!EMAIL_PATTERN.matcher(email).matches()){
            throw new GeneralException(ErrorStatus.INVALID_EMAIL);
        }
    }

    // 전화번호 유효성 검사
    private void validatePhoneNumber(String phoneNum) {
        if (!PHONE_NUMBER_PATTERN.matcher(phoneNum).matches()) {
            throw new GeneralException(ErrorStatus.INVALID_PHONE_NUMBER);
        }
    }
}
