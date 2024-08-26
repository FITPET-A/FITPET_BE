package FITPET.dev.common.utils;

import FITPET.dev.common.annotation.ExcelColumn;
import FITPET.dev.common.basecode.ErrorStatus;
import FITPET.dev.common.exception.GeneralException;
import FITPET.dev.dto.response.InsuranceResponse;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
public class ExcelUtils implements ExcelUtilFactory{
    @Override
    public void downloadInsurances(HttpServletResponse response, List<InsuranceResponse.InsuranceDetailExcelDto> data) {
        // 엑셀파일(Workbook) 객체 생성
        Workbook workbook = getXSSFWorkBook();

        // 엑셀파일 sheet를 만들고, sheet의 이름 지정
        Sheet sheet = workbook.createSheet("sheet1");

        // 엑셀이 Header에 들어갈 내용 추출
        List<String> excelHeaderList = getHeaderName(getClass(data));

        // 엑셀의 첫 번째 행에 해당하는 Row 객체 생성
        Row row = sheet.createRow(0);

        // 엑셀의 열에 해당하는 Cell 객체 생성
        Cell cell = null;

        // 헤더의 수(컬럼 이름의 수)만큼 반복해서 행 생성
        for(int i=0; i<excelHeaderList.size(); i++) {

            cell = row.createCell(i);

            // 열에 헤더이름(컬럼 이름)을 지정
            cell.setCellValue(excelHeaderList.get(i));
        }

        // 헤더 아래에 들어갈 내용을 그림
        createInsurancesBody(data, sheet, row, cell);

        response.setContentType("ms-vnd/excel");
        response.setHeader("Content-Disposition", "attachment; filename=" + "SC_회사별_보험_정보" + ".xlsx");

        try {
            // 엑셀 파일을 다운로드
            ServletOutputStream outputStream = response.getOutputStream();
            workbook.write(outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    /*
     * 엑셀 본문에 header 아래에 들어갈 Insurance Body를 그림
     * @param data
     * @param sheet
     * @param row
     * @param cell
     */
    private void createInsurancesBody(
            List<InsuranceResponse.InsuranceDetailExcelDto> data, Sheet sheet, Row row, Cell cell){

        int rowIndex = 1;

        for(InsuranceResponse.InsuranceDetailExcelDto insurance : data) {

            List<Object> fields = getFieldValues(getClass(data), insurance);

            // 1번 인덱스부터 행 생성
            row = sheet.createRow(rowIndex++);

            for (int i = 0, fieldSize = fields.size(); i < fieldSize; i++) {
                cell = row.createCell(i);

                // 열 데이터 지정
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
        if(!data.isEmpty()) {
            return data.get(data.size()-1).getClass();
        } else {
            throw new IllegalStateException("조회된 리스트가 비어 있습니다.");
        }
    }


    private List<Object> getFieldValues(Class<?> clazz, Object obj) {
        try {
            List<Object> result = new ArrayList<>();
            for (Field field : clazz.getDeclaredFields()) {
                field.setAccessible(true);
                result.add(field.get(obj));
            }
            return result;
        } catch (Exception e){
            throw new GeneralException(ErrorStatus.FAILURE_RENDER_EXCEL_BODY);
        }
    }

}
