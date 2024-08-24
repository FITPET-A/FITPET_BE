package FITPET.dev.service;

import FITPET.dev.common.basecode.ErrorStatus;
import FITPET.dev.common.enums.*;
import FITPET.dev.common.exception.GeneralException;
import FITPET.dev.converter.InsuranceConverter;
import FITPET.dev.entity.Insurance;
import FITPET.dev.repository.InsuranceRepository;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;

@Service
@RequiredArgsConstructor
public class InitService {
    private final InsuranceRepository insuranceRepository;

    // excel file을 DB에 저장
    @Transactional
    public void initDatabase(){
        // 강아지 보험 entity parsing
        initDogInsurance();

        // 고양이 보험 entity parsing
        initCatInsurance();

        // 통합견종리스트 parsing
        initDogBreedList();

        // 묘종리스트 parsing
        initCatBreedList();
    }

    private void initDogInsurance() {
        // read excel file
        Workbook dogInsurance = readExcelFile("assets/SCins_펫보험견적서통합본_240806_전달용.xlsx");

        // read excel file's sheet and parse insurance data
        parseDogInsuranceSheet(dogInsurance, Company.MERITZ, 9);
        parseDogInsuranceSheet(dogInsurance, Company.DB, 10);
        parseDogInsuranceSheet(dogInsurance, Company.HYUNDAE, 11);
        parseDogInsuranceSheet(dogInsurance, Company.KB, 12);
        parseDogInsuranceSheet(dogInsurance, Company.SAMSUNG, 13);
    }

    private void parseDogInsuranceSheet(Workbook workbook, Company company, int sheetNum){
        Sheet sheet = readExcelSheet(workbook, sheetNum);

        for (int i = 7; i < sheet.getLastRowNum(); i++){
            Row row = sheet.getRow(i);

            if (row == null) continue;

            // read cell data and parse enum data
            int age = parseNumericCellValue(row, 0);
            String dogBreedRank = parseStringCellValue(row, 1);
            String renewalCycleString = parseStringCellValue(row, 2);
            String coverageRatioString = parseStringCellValue(row, 3);
            String deductibleString = parseStringCellValue(row, 4);
            String compensationString = parseStringCellValue(row, 5);
            int premium = parseNumericCellValue(row, 7);

            // parse enum data
            RenewalCycle renewalCycle = RenewalCycle.getRenewalCycle(renewalCycleString);
            CoverageRatio coverageRatio = CoverageRatio.getCoverageRatio(coverageRatioString);
            Deductible deductible = Deductible.getDeductible(deductibleString);
            Compensation compensation = Compensation.getCompensation(compensationString);

            // create insurance entity
            Insurance insurance = InsuranceConverter.toDogInsurance(
                    company, age, dogBreedRank, renewalCycle,
                    coverageRatio, deductible, compensation, premium);

            System.out.println(insurance.toString());

            insuranceRepository.save(insurance);
        }
    }

    private int parseNumericCellValue(Row row, int cellNum){
        int value = 0;
        try{
            value = (int) row.getCell(cellNum).getNumericCellValue();
        } catch (NullPointerException e){
            return value;
        }
        catch (Exception e){
            if (cellNum != 7) return 0;

            String valueString = row.getCell(7).getStringCellValue();
            if (valueString.equals("인수제한"))
                value = -1;
        }
        return value;
    }

    private String parseStringCellValue(Row row, int cellNum){
        String value;
        try{
            value = row.getCell(cellNum).getStringCellValue();
        } catch (Exception e){
            value = "";
        }
        return value;
    }

    private void initCatInsurance() {
    }

    private void initDogBreedList() {
    }

    private void initCatBreedList() {
    }

    private Workbook readExcelFile(String path) {
        try {
            ClassPathResource classPathResource = new ClassPathResource(path);
            InputStream inputStream = classPathResource.getInputStream();
            return new XSSFWorkbook(inputStream);
        } catch (Exception e){
            throw new GeneralException(ErrorStatus.FAILURE_READ_EXCEL_FILE);
        }
    }

    private Sheet readExcelSheet(Workbook workbook, int sheet){
        try{
            return workbook.getSheetAt(sheet);
        } catch (Exception e){
            throw new GeneralException(ErrorStatus.FAILURE_READ_EXCEL_SHEET);
        }
    }
}
