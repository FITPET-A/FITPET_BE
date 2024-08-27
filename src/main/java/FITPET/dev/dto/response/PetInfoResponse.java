package FITPET.dev.dto.response;

import FITPET.dev.common.annotation.ExcelColumn;
import FITPET.dev.common.annotation.ExcelFile;
import FITPET.dev.common.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class PetInfoResponse {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @ExcelFile
    public static class PetInfoExcelDto {
        private Status status;
        @ExcelColumn(headerName = "견적서Id")
        private Long petInfoId;
        @ExcelColumn(headerName = "견적 요청 일시")
        private LocalDateTime createdAt;
        @ExcelColumn(headerName = "품종")
        private String petType;
        @ExcelColumn(headerName = "상세 품종")
        private String detailType;
        @ExcelColumn(headerName = "이름")
        private String name;
        @ExcelColumn(headerName = "나이")
        private int age;
        @ExcelColumn(headerName = "보호자 연락처")
        private String phoneNum;
    }


}
