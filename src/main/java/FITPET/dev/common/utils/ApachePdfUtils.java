package FITPET.dev.common.utils;

import FITPET.dev.common.enums.PetType;
import FITPET.dev.common.exception.GeneralException;
import FITPET.dev.common.status.ErrorStatus;
import FITPET.dev.dto.response.InsuranceResponse;
import FITPET.dev.entity.Comparison;
import FITPET.dev.entity.Pet;
import FITPET.dev.entity.PetInfo;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Slf4j
@Component
@NoArgsConstructor
public class ApachePdfUtils {
    private final static File fontFile = new File("src/main/resources/fonts/Pretendard.ttf");


    /*
     * 견적서 PDF 파일을 추출함
     * @param response
     * @param comparison
     */
    public void downloadComparisonPdf(HttpServletResponse response, Comparison comparison,
                                      InsuranceResponse.AllInsuranceListDto allInsuranceListDto) {

        String backgroundPath = getBackgroundPath(comparison.getPetInfo().getPet());

        // 배경용 PDF 파일 로드
        PDDocument doc = loadPDDocument(backgroundPath);

        // 보험지급건 PDF 파일 로드
        PDDocument doc2 = loadPDDocument("src/main/resources/assets/보험지급건.pdf");

        // 배경용 PDF 편집
        drawComparisonPdf(doc, comparison, allInsuranceListDto);

        // 파일 이름 생성
        String fileName = getPdfFileName(comparison);

        // 두 개의 PDF 병합 및 다운로드
        mergeAndDownloadPdf(response, fileName, doc, doc2);
    }


    private void drawComparisonPdf(PDDocument doc, Comparison comparison,
                                   InsuranceResponse.AllInsuranceListDto allInsuranceListDto) {
        PDPage page = doc.getPage(0);

        try {
            PDPageContentStream pageContentStream = new PDPageContentStream(doc, page, PDPageContentStream.AppendMode.APPEND, true, true);
            PDFont font = PDType0Font.load(doc, fontFile);
            ContentStream contentStream = new ContentStream(pageContentStream, font);

            // 견적 요청 일시 정보 편집
            drawComparisonCreatedAt(contentStream, comparison.getCreatedAt());

            // 반려동물 정보 편집
            drawPetInfo(contentStream, comparison.getPetInfo());

            // 보험료 정보 편집
            PetType petType = comparison.getPetInfo().getPet().getPetType();
            drawInsuranceInfos(contentStream, allInsuranceListDto, petType);

            contentStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private void drawComparisonCreatedAt(ContentStream contentStream, LocalDateTime comparisonCreatedAt){
        int x = 96;
        float y = (float) (1446 + 7.5);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일");
        String formattedComparisonCreatedAt = comparisonCreatedAt.format(formatter);

        contentStream.setColor(1.0f, 1.0f, 1.0f); // white
        contentStream.setFontSize(24);
        contentStream.writeText(x, y, formattedComparisonCreatedAt);
    }


    private void drawPetInfo(ContentStream contentStream, PetInfo petInfo){
        int petInfoX = 746;
        int nameY = 1537 + 2;
        int ageY = 1493 + 2;
        int petSpeciesY = 1449 + 2;

        contentStream.setColor(1.0f, 1.0f, 1.0f); // white

        // name
        contentStream.setFontSize(38);
        contentStream.writeText(petInfoX, nameY, petInfo.getName());

        // age
        contentStream.setFontSize(24);
        contentStream.writeText(petInfoX, ageY, "만 " + petInfo.getAge() + "세");

        // petSpecies
        contentStream.writeText(petInfoX, petSpeciesY, petInfo.getPet().getPetSpecies());
    }


    private void drawInsuranceInfos(ContentStream contentStream, InsuranceResponse.AllInsuranceListDto allInsuranceListDto, PetType petType) {
        int seventyY = 1237 + 2;
        int eightyY = 1146 + 2;
        int ninetyY = 1055 + 2;

        float dogLabelWidth = 160F;
        float catLabelWidth = 225.5F;
        float labelWidth = PetType.isDog(petType) ? dogLabelWidth : catLabelWidth;

        for (InsuranceResponse.InsuranceDto insuranceDto : allInsuranceListDto.getSeventyinsuranceDtoList()){
            writePremiumInfo(contentStream, insuranceDto, seventyY, labelWidth);
        }
        for (InsuranceResponse.InsuranceDto insuranceDto : allInsuranceListDto.getEightyinsuranceDtoList()){
            writePremiumInfo(contentStream, insuranceDto, eightyY, labelWidth);
        }
        for (InsuranceResponse.InsuranceDto insuranceDto : allInsuranceListDto.getNinetyinsuranceDtoList()){
            writePremiumInfo(contentStream, insuranceDto, ninetyY, labelWidth);
        }
    }


    private void writePremiumInfo(ContentStream contentStream, InsuranceResponse.InsuranceDto insuranceDto, float Y, float labelWidth){
        // premium
        contentStream.setColor(0f, 0.549f, 1.0f);
        contentStream.setFontSize(20);

        float x = getInsuranceCompanyInfoXValue(insuranceDto.getCompany(), labelWidth) + (labelWidth / 2);
        String text = insuranceDto.getPremium() + "원";
        float centerX = getCenterX(x, text, contentStream.getFont(), 20);

        contentStream.writeText(centerX, Y, text);

        // discountedPremium
        contentStream.setColor(0.588f, 0.588f, 0.651f);
        contentStream.setFontSize(18);

        float discountedY = Y - 31;
        String discountedtext = insuranceDto.getDiscountedPremium() + "원";
        float discountedCenterX = getCenterX(x, discountedtext, contentStream.getFont(), 18);

        contentStream.writeText(discountedCenterX, discountedY, discountedtext);
    }

    private float getInsuranceCompanyInfoXValue(String company, float labelWidth){
        PetType petType = labelWidth == 160F ? PetType.DOG : PetType.CAT;

        float meritzX = 234F + 4;
        float dbX = PetType.isDog(petType) ? 414.4F + 4 : 459.5F + 4;
        float hyndaeX = PetType.isDog(petType) ? 594.8F + 4 : 685F + 4;
        float kbX = PetType.isDog(petType) ? 775.2F + 4 : 910F + 4;
        float samsungX = PetType.isDog(petType) ? 955.6F + 4 : -1;

        return switch (company) {
            case "메리츠" -> meritzX;
            case "DB" -> dbX;
            case "현대" -> hyndaeX;
            case "KB" -> kbX;
            case "삼성" -> samsungX;
            default -> -1;
        };
    }


    private float getCenterX(float x, String text, PDFont font, int fontSize){
        float textWidth = 0;
        try {
            // 가운데 정렬 X좌표 계산
            textWidth = font.getStringWidth(text) / 1000 * fontSize;
            return x - (textWidth / 2);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private String getPdfFileName(Comparison comparison) {
        PetInfo petInfo = comparison.getPetInfo();

        // 요청 일시 포맷 변경
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMdd");
        String formattedDate = comparison.getCreatedAt().format(formatter);

        return petInfo.getPhoneNum().substring(9, 13) + "_" +
                petInfo.getName() + "_" +
                petInfo.getPet().getPetSpecies() + "_" +
                formattedDate;
    }


    private void mergeAndDownloadPdf(HttpServletResponse response, String fileName, PDDocument doc, PDDocument doc2) {
        try {
            // 임시 파일 생성
            File tempFile1 = File.createTempFile("temp1", ".pdf");
            File tempFile2 = File.createTempFile("temp2", ".pdf");

            // PDF 문서를 임시 파일에 저장
            doc.save(tempFile1);
            doc2.save(tempFile2);

            // PDF 병합을 위한 PDFMergerUtility 생성
            PDFMergerUtility merger = new PDFMergerUtility();
            merger.addSource(tempFile1);
            merger.addSource(tempFile2);

            // 병합된 PDF를 outputStream으로 저장
            ServletOutputStream outputStream = setServletOutputStream(response, fileName);
            merger.setDestinationStream(outputStream);
            merger.mergeDocuments(null);

            // 입출력 스트림을 닫음
            doc.close();
            doc2.close();
            outputStream.close();

            // 임시 파일 삭제
            tempFile1.delete();
            tempFile2.delete();

        } catch (IOException e) {
            throw new RuntimeException(e);
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
            throw new GeneralException(ErrorStatus.FAILURE_ENCODE_PDF_FILE_NAME);
        }
    }


    private String getBackgroundPath(Pet pet) {
        String path = "src/main/resources/assets/";
        return pet.getPetType() == PetType.DOG ? path + "dog_background.pdf" : path + "cat_background.pdf";
    }


    private PDDocument loadPDDocument(String path){
        try{
            return Loader.loadPDF(new File(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
