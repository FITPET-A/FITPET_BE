package FITPET.dev.common.utils;

import FITPET.dev.common.enums.Company;
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
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;


@Slf4j
@Component
@NoArgsConstructor
public class ApachePdfUtils {

    /*
     * 견적서 PDF 파일을 추출함
     * @param response
     * @param comparison
     */
    public void downloadComparisonPdf(HttpServletResponse response, Comparison comparison,
                                      InsuranceResponse.AllInsuranceListDto allInsuranceListDto) {

        String backgroundPath = getBackgroundPath(comparison.getPetInfo().getPet());

        // 배경용 PDF 파일 로드
        PDDocument doc = loadPDDocument("assets/" + backgroundPath);

        // 보험지급건 PDF 파일 로드
        PDDocument doc2 = loadPDDocument("assets/보험지급건.pdf");

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
            File fontFile = loadFontFile("fonts/Pretendard.ttf");
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
        int nameY = 1537 + 4;
        int ageY = 1493 + 4;
        int petSpeciesY = 1449 + 4;

        contentStream.setColor(1.0f, 1.0f, 1.0f); // white

        // name
        contentStream.setFontSize(38);
        contentStream.writeText(petInfoX, nameY, petInfo.getName());

        // age
        contentStream.setFontSize(22);
        contentStream.writeText(petInfoX, ageY, "만 " + petInfo.getAge() + "세");

        // petSpecies
        contentStream.writeText(petInfoX, petSpeciesY, petInfo.getPet().getPetSpecies());
    }


    private void drawInsuranceInfos(ContentStream contentStream, InsuranceResponse.AllInsuranceListDto allInsuranceListDto, PetType petType) {
        int seventyY = 1237 + 2;
        int eightyY = 1146 + 2;
        int ninetyY = 1055 + 2;

        float dogLabelWidth = 160F;
        float catLabelWidth = 225F;
        float labelWidth = PetType.isDog(petType) ? dogLabelWidth : catLabelWidth;

        // 70% 보험료 작성
        HashMap<Company, Integer> isValueExistMap = initIsCompanyValueExistMap();
        for (InsuranceResponse.InsuranceDto insuranceDto : allInsuranceListDto.getSeventyinsuranceDtoList()){
            isValueExistMap.put(Company.getCompany(insuranceDto.getCompany()), 1);
            writePremiumInfos(contentStream, insuranceDto, seventyY, labelWidth);
        }
        checkValueExist(isValueExistMap, contentStream, seventyY, labelWidth);

        // 80% 보험료 작성
        for (InsuranceResponse.InsuranceDto insuranceDto : allInsuranceListDto.getEightyinsuranceDtoList()){
            isValueExistMap.put(Company.getCompany(insuranceDto.getCompany()), 1);
            writePremiumInfos(contentStream, insuranceDto, eightyY, labelWidth);
        }
        checkValueExist(isValueExistMap, contentStream, eightyY, labelWidth);

        // 90% 보험료 작성
        for (InsuranceResponse.InsuranceDto insuranceDto : allInsuranceListDto.getNinetyinsuranceDtoList()){
            isValueExistMap.put(Company.getCompany(insuranceDto.getCompany()), 1);
            writePremiumInfos(contentStream, insuranceDto, ninetyY, labelWidth);
        }
        checkValueExist(isValueExistMap, contentStream, ninetyY, labelWidth);
    }


    private void checkValueExist(HashMap<Company, Integer> map, ContentStream contentStream, float Y, float labelWidth){
        for (Map.Entry<Company, Integer> entry : map.entrySet()) {
            if (entry.getValue() == 0) {

                // 고양이 품종 & 삼성 정보 기입 X
                if (entry.getKey().getLabel().equals("삼성") && labelWidth != 160F)
                    continue;

                // 가입불가 메세지 작성
                writeDiscountedPremium(contentStream, entry.getKey().getLabel(), -1, Y, labelWidth);
                writeOriginalPremiumAndStroke(contentStream, entry.getKey().getLabel(), -1, -1, Y, labelWidth);
            }
        }

        clearHashMap(map);
    }


    private void writePremiumInfos(ContentStream contentStream, InsuranceResponse.InsuranceDto insuranceDto, float Y, float labelWidth){
        String company = insuranceDto.getCompany();
        int premium = insuranceDto.getPremium();
        int discountedPremium = insuranceDto.getDiscountedPremium();

        if (company.equals("삼성") && labelWidth != 160F)
            return;

        float x = writeDiscountedPremium(contentStream, company, discountedPremium, Y, labelWidth);
        writeOriginalPremiumAndStroke(contentStream, company, premium, x, Y, labelWidth);
    }


    private float writeDiscountedPremium(ContentStream contentStream, String company, int premium, float Y, float labelWidth){
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        contentStream.setColor(0f, 0.549f, 1.0f);
        contentStream.setFontSize(20);

        // 좌표 설정
        float x = getInsuranceCompanyInfoXValue(company, labelWidth) + (labelWidth / 2) - 2;
        // 문자열 및 중앙정렬을 위한 X좌표 설정
        String text = premium == -1 ? "가입 불가" : decimalFormat.format(premium) + "원";
        float centerX = getCenterX(x, text, contentStream.getFont(), 20);
        contentStream.writeText(centerX, Y, text);

        return x;
    }

    private void writeOriginalPremiumAndStroke(ContentStream contentStream, String company, int premium, float X, float Y, float labelWidth){
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        contentStream.setColor(0.588f, 0.588f, 0.651f);
        contentStream.setFontSize(18);

        // 좌표 설정
        Y = Y - 31;
        if (X == -1)
            X = getInsuranceCompanyInfoXValue(company, labelWidth) + (labelWidth / 2) - 2;

        // 문자열 및 중앙정렬을 위한 X좌표 설정
        String text = premium == -1 ? "가입 불가" : decimalFormat.format(premium) + "원";
        float centerX = getCenterX(X, text, contentStream.getFont(), 18);
        contentStream.writeText(centerX, Y, text);

        // stroke 작성
        float textWidth = getStringWidth(text, contentStream.getFont(), 18);
        contentStream.drawStroke(centerX, Y + 6, textWidth);
    }


    private float getInsuranceCompanyInfoXValue(String company, float labelWidth){
        PetType petType = labelWidth == 160F ? PetType.DOG : PetType.CAT;

        float meritzX = 234F;
        float dbX = PetType.isDog(petType) ? 414.4F : 459.5F;
        float hyndaeX = PetType.isDog(petType) ? 594.8F : 685F;
        float kbX = PetType.isDog(petType) ? 775.2F : 910F;
        float samsungX = PetType.isDog(petType) ? 955.6F : -1;

        // 회사, 견종에 따라 x좌표 값 리턴
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
        float textWidth = getStringWidth(text, font, fontSize);
        return x - (textWidth / 2);
    }


    private float getStringWidth(String text, PDFont font, int fontSize){
        try {
            return font.getStringWidth(text) / 1000 * fontSize;
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
        return pet.getPetType() == PetType.DOG ? "dog_background.pdf" : "cat_background.pdf";
    }


    private PDDocument loadPDDocument(String path){
        ClassPathResource pdfResource = new ClassPathResource(path);
        try (InputStream inputStream = pdfResource.getInputStream()) {
            return Loader.loadPDF(inputStream.readAllBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private File loadFontFile(String path){
        try {
            ClassPathResource resource = new ClassPathResource(path);
            return resource.getFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private HashMap<Company, Integer> initIsCompanyValueExistMap() {
        return new HashMap<Company, Integer>() {{
            put(Company.MERITZ, 0);
            put(Company.SAMSUNG, 0);
            put(Company.DB, 0);
            put(Company.HYUNDAE, 0);
            put(Company.KB, 0);
        }};
    }

    private void clearHashMap(HashMap<Company, Integer> hashMap){
        // 모든 값을 0으로 설정
        for (Map.Entry<Company, Integer> entry : hashMap.entrySet()) {
            entry.setValue(0);
        }
    }

}