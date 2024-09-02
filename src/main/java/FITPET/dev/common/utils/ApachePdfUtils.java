package FITPET.dev.common.utils;

import FITPET.dev.common.exception.GeneralException;
import FITPET.dev.common.status.ErrorStatus;
import FITPET.dev.entity.Comparison;
import FITPET.dev.entity.PetInfo;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.time.format.DateTimeFormatter;

@Slf4j
@Component
public class ApachePdfUtils {

    public void downloadComparisonPdf(HttpServletResponse response, Comparison comparison) {
        String fileName = parsePdfFileName(comparison);

        PDDocument doc = new PDDocument();

        // pdf 다운로드 및 outputStream 종료
        downloadPdf(response, fileName, doc);
    }


    private String parsePdfFileName(Comparison comparison) {
        PetInfo petInfo = comparison.getPetInfo();

        // 요청 일시 포맷 변경
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMdd");
        String formattedDate = comparison.getCreatedAt().format(formatter);

        return petInfo.getPhoneNum().substring(9, 13) + "_" +
               petInfo.getName() + "_" +
                petInfo.getPet().getPetSpecies() + "_" +
                formattedDate;
    }

    private void downloadPdf(HttpServletResponse response, String fileName, PDDocument doc) {
        try {
            // 파일 형식 및 이름을 지정 후 다운로드를 위해 outputStream 반환
            ServletOutputStream outputStream = setServletOutputStream(response, fileName);

            // pdf 파일을 다운로드
            doc.save(outputStream);

            // 입출력 스트림을 닫음
            doc.close();
        } catch (IOException e) {

        }
    }


    private ServletOutputStream setServletOutputStream(HttpServletResponse response, String fileName){
        try {
            // 파일 다운로드를 위해 파일 형식 지정
            response.setContentType("application/pdf");
            response.setCharacterEncoding("UTF-8");

            // 파일 이름 지정
            String encodedFileName = encodePdfFileName(fileName);
            response.setHeader("Content-Disposition", "inline; filename=\"" + encodedFileName + ".pdf\"; filename*=UTF-8''" + encodedFileName + ".pdf");

            return response.getOutputStream();
        } catch (IOException e){
            throw new RuntimeException(e);
        }
    }


    private String encodePdfFileName(String fileName) {
        try{
            return URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
        } catch (Exception e){
            throw new GeneralException(ErrorStatus.FAILURE_ENCODE_EXCEL_FILE_NAME);
        }
    }

}
