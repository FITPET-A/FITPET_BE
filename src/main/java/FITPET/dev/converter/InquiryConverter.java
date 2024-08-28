package FITPET.dev.converter;

import FITPET.dev.entity.Inquiry;

public class InquiryConverter {
    public static Inquiry toInquiry(String name, String email, String phoneNum, String comment){
        return Inquiry.builder()
                .name(name)
                .email(email)
                .phoneNum(phoneNum)
                .comment(comment)
                .build();
    }
}
