package FITPET.dev.converter;

import FITPET.dev.common.enums.InquiryStatus;
import FITPET.dev.dto.response.InquiryResponse;
import FITPET.dev.entity.Inquiry;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;

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
                .status(InquiryStatus.PENDING)
                .build();
    }

    public static InquiryResponse.InquiryDto toInquiryDto(Inquiry inquiry){
        return InquiryResponse.InquiryDto.builder()
                .createdAt(formatDateTime(inquiry.getCreatedAt()))
                .inquiryId(inquiry.getInquiryId())
                .name(inquiry.getName())
                .email(inquiry.getEmail())
                .phoneNum(inquiry.getPhoneNum())
                .comment(inquiry.getComment())
                .status(inquiry.getStatus().toString())
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

    public static InquiryResponse.InquiryPageDto toInquiryPageDto(Page<Inquiry> page) {
        List<InquiryResponse.InquiryDto> inquiryDtoList = page.getContent().stream()
                .map(InquiryConverter::toInquiryDto)
                .collect(Collectors.toList());

        return InquiryResponse.InquiryPageDto.builder()
                .content(inquiryDtoList)
                .currentPage(page.getNumber() + 1)
                .pageSize(page.getSize())
                .numberOfElement(page.getNumberOfElements())
                .totalElements((int) page.getTotalElements())
                .totalPage(page.getTotalPages())
                .build();
    }

}
