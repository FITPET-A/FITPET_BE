package FITPET.dev.converter;

import FITPET.dev.dto.response.InquiryResponse;
import FITPET.dev.entity.Inquiry;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class InquiryConverter {

    public static String formatDateTime(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return dateTime.format(formatter);
    }

    public static Inquiry toInquiry(String name, String email, String phoneNum, String comment){
        return Inquiry.builder()
                .name(name)
                .email(email)
                .phoneNum(phoneNum)
                .comment(comment)
                .build();
    }

    public static InquiryResponse.InquiryDto toInquiryDto(Inquiry inquiry){
        return InquiryResponse.InquiryDto.builder()
                .createdAt(formatDateTime(inquiry.getCreated_at()))
                .inquiryId(inquiry.getInquiryId())
                .name(inquiry.getName())
                .email(inquiry.getEmail())
                .phoneNum(inquiry.getPhoneNum())
                .comment(inquiry.getComment())
                .build();
    }

    public static InquiryResponse.InquiryListDto toInquiryListDto(List<Inquiry> inquiryList) {
        List<InquiryResponse.InquiryDto> inquiryDtoList = inquiryList.stream()
                .map(InquiryConverter::toInquiryDto)
                .toList();

        return InquiryResponse.InquiryListDto.builder()
                .inquiryDtoList(inquiryDtoList)
                .build();
    }
}
