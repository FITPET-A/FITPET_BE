package FITPET.dev.common.utils;

import FITPET.dev.common.annotation.ExcelColumn;
import FITPET.dev.common.basecode.ErrorStatus;
import FITPET.dev.common.exception.GeneralException;
import FITPET.dev.dto.response.InsuranceResponse;
import FITPET.dev.dto.response.PetInfoResponse;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
public class ExcelUtils implements ExcelUtilFactory {
    @Override
    public void downloadInsurances(HttpServletResponse response, List<InsuranceResponse.InsuranceExcelDto> data) {

        final String fileName = "SC_회사별_보험_정보";

        // 엑셀파일(Workbook) 객체 생성
        Workbook workbook = getXSSFWorkBook();

        // 데이터를 회사별로 그룹화
        Map<String, List<InsuranceResponse.InsuranceExcelDto>> groupedByCompany = data.stream()
                .collect(Collectors.groupingBy(InsuranceResponse.InsuranceExcelDto::getCompany));

        // 회사별로 시트 생성 및 데이터 작성
        groupedByCompany.forEach((companyName, companyData) -> {
            Sheet sheet = workbook.createSheet(companyName);

            // 엑셀 Header 생성
            createHeaderRow(sheet, companyData);

            // 헤더 아래에 들어갈 내용을 그림
            createInsurancesBody(companyData, sheet);
        });

        // workbook(엑셀 파일) 다운로드 및 outputStream 종료
        downloadWorkBook(response, fileName, workbook);
    }


    @Override
    public void downloadPetInfos(HttpServletResponse response, List<PetInfoResponse.PetInfoExcelDto> data) {

        final String fileName = "SC_견적_요청_정보";

        // 엑셀파일(Workbook) 객체 생성
        Workbook workbook = getXSSFWorkBook();

        Sheet sheet = workbook.createSheet("sheet1");

        // 엑셀 Header 생성
        createHeaderRow(sheet, data);

        // 헤더 아래에 들어갈 내용을 그림
        createInsurancesBody(data, sheet);

        // workbook(엑셀 파일) 다운로드 및 outputStream 종료
        downloadWorkBook(response, fileName, workbook);
    }


    /*
     * 엑셀 헤더(Column) 설정
     * @param sheet
     * @param data
     */
    private void createHeaderRow(Sheet sheet, List<?> data) {
        // 엑셀의 Header에 들어갈 내용 추출
        List<String> excelHeaderList = getHeaderName(getClass(data));

        // 엑셀의 첫 번째 행에 해당하는 Row 객체 생성
        Row row = sheet.createRow(0);

        // 헤더의 수(컬럼 이름의 수)만큼 반복해서 행 생성
        for (int i = 0; i < excelHeaderList.size(); i++) {

            // 엑셀의 열에 해당하는 Cell 객체 생성
            Cell cell = row.createCell(i);

            // 열에 헤더이름(컬럼 이름)을 지정
            cell.setCellValue(excelHeaderList.get(i));
        }
    }


    /*
     * 엑셀 본문에 header 아래에 들어갈 Insurance Body를 그림
     * @param data
     * @param sheet
     * @param row
     * @param cell
     */
    private void createInsurancesBody(List<?> data, Sheet sheet) {

        // 시작 행 인덱스 지정
        int rowIndex = 1;

        for (Object obj : data) {

            // object data의 필드값들을 List로 반환
            List<Object> fields = getFieldValues(getClass(data), obj);

            // 1번 인덱스부터 행 생성
            Row row = sheet.createRow(rowIndex++);

            for (int i = 0, fieldSize = fields.size(); i < fieldSize; i++) {

                Cell cell = row.createCell(i);

                // 각 열에 순차적으로 insurance 필드값 지정
                cell.setCellValue(String.valueOf(fields.get(i)));
            }
        }
    }


    /*
     * 엑셀의 WorkBook 객체를 반환
     * @return XSSFWorkBook
     */
    private Workbook getXSSFWorkBook() {
        return new XSSFWorkbook();
    }


    /*
     * 엑셀 헤더 이름들을 리스트로 반환
     * @param Class<?>
     * @return List<String>
     */
    private List<String> getHeaderName(Class<?> type) {

        // 1. 매개변수로 전달된 클래스의 필드들을 배열로 받아, 스트림 생성
        // 2. @ExcelColumn 애너테이션이 붙은 필드만 필터링
        // 3. @ExcelColumn 애너테이션이 붙은 필드에서 애너테이션의 값 매핑
        // 4. List로 반환

        return Arrays.stream(type.getDeclaredFields())
                .filter(s -> s.isAnnotationPresent(ExcelColumn.class))
                .map(s -> s.getAnnotation(ExcelColumn.class).headerName())
                .toList();
    }


    /*
     *   List(데이터 리스트)에 담긴 DTO의 클래스 정보를 반환하는 메서드
     *   @param List<?>
     *   @return Class<?>
     * */
    private Class<?> getClass(List<?> data) {
        // List가 비어있지 않다면 맨 마지막 element의 클래스 정보 반환
        if (!data.isEmpty()) {
            return data.get(data.size() - 1).getClass();
        } else {
            throw new IllegalStateException("조회된 리스트가 비어 있습니다.");
        }
    }


    /*
     * obj에 선언된 필드들의 값을 추출해 List로 반환
     * @param clazz
     * @param obj
     * @return
     */
    private List<Object> getFieldValues(Class<?> clazz, Object obj) {
        try {
            List<Object> result = new ArrayList<>();

            // 클래스에 선언되어 있는 필드들을 List에 추가
            for (Field field : clazz.getDeclaredFields()) {

                // @ExcelColumn 어노테이션이 붙은 칼럼만 추출
                if (field.isAnnotationPresent(ExcelColumn.class)) {
                    field.setAccessible(true);
                    result.add(field.get(obj));
                }
            }
            return result;

        } catch (Exception e) {
            throw new GeneralException(ErrorStatus.FAILURE_RENDER_EXCEL_BODY);
        }
    }


    /*
     * 엑셀 파일을 다운로드하고 outputStream을 종료함
     * @param response
     * @param fileName
     * @param workbook
     */
    private void downloadWorkBook(HttpServletResponse response, String fileName, Workbook workbook) {
        try {
            // 파일 형식 및 이름을 지정 후 다운로드를 위해 outputStream 반환
            ServletOutputStream outputStream = setServletOutputStream(response, fileName);

            // 엑셀 파일을 다운로드
            workbook.write(outputStream);

            // 입출력 스트림을 닫음
            outputStream.flush();
            outputStream.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    /*
     * 엑셀 파일 다운로드를 위해 Header, 파일 이름, 파일 형식을 지정함
     * @param response
     * @param fileName
     * @return
     */
    private ServletOutputStream setServletOutputStream(HttpServletResponse response, String fileName){
        try {
            // 파일 다운로드를 위해 파일 형식 지정
            response.setContentType("ms-vnd/excel");
            response.setCharacterEncoding("UTF-8");

            // 파일 이름 지정
            String encodedFileName = encodeExcelFileName(fileName);
            response.setHeader("Content-Disposition", "attachment; filename*=UTF-8''" + encodedFileName + ".xlsx");

            return response.getOutputStream();
        } catch (IOException e){
            throw new RuntimeException(e);
        }
    }


    private String encodeExcelFileName(String fileName) {
        try{
            return URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
        } catch (Exception e){
            throw new GeneralException(ErrorStatus.FAILURE_ENCODE_EXCEL_FILE_NAME);
        }

    }

}
