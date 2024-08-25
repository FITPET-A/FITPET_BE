package FITPET.dev.service;

import FITPET.dev.common.basecode.ErrorStatus;
import FITPET.dev.common.enums.*;
import FITPET.dev.common.exception.GeneralException;
import FITPET.dev.converter.DogBreedConverter;
import FITPET.dev.converter.InsuranceConverter;
import FITPET.dev.converter.PetConverter;
import FITPET.dev.entity.CompanyDogBreed;
import FITPET.dev.entity.DogBreedDetail;
import FITPET.dev.entity.Insurance;
import FITPET.dev.entity.Pet;
import FITPET.dev.repository.CompanyDogBreedRepository;
import FITPET.dev.repository.DogBreedDetailRepository;
import FITPET.dev.repository.InsuranceRepository;
import FITPET.dev.repository.PetRepository;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class InitService {
    private final InsuranceRepository insuranceRepository;
    private final PetRepository petRepository;
    private final CompanyDogBreedRepository companyDogBreedRepository;
    private final DogBreedDetailRepository dogBreedDetailRepository;


    // excel file을 DB에 저장
    @Transactional
    public void initDatabase(){
        // 강아지 보험 entity parsing
        initInsuranceData(PetType.DOG, new int[]{9, 10, 11, 12, 13});

        // 고양이 보험 entity parsing
        initInsuranceData(PetType.CAT, new int[]{15, 16, 17, 18});

        // 통합견종리스트 parsing
        initDogBreedList();

        // 묘종리스트 parsing
        initCatBreedList();
    }

    private void initInsuranceData(PetType petType, int[] sheetNumbers) {
        Workbook workbook = readExcelFile("assets/SCins_펫보험견적서통합본_240806_전달용.xlsx");
        Company[] companies = PetType.isDog(petType)
                ? new Company[]{Company.MERITZ, Company.DB, Company.HYUNDAE, Company.KB, Company.SAMSUNG}
                : new Company[]{Company.MERITZ, Company.DB, Company.KB, Company.HYUNDAE};

        for (int i = 0; i < sheetNumbers.length; i++) {
            parseInsuranceSheet(workbook, companies[i], sheetNumbers[i], petType);
        }
    }

    private void parseInsuranceSheet(Workbook workbook, Company company, int sheetNum, PetType petType) {
        Sheet sheet = readExcelSheet(workbook, sheetNum);

        int startIndex = (PetType.isDog(petType) || company == Company.MERITZ) ? 6 : 4;
        for (int i = startIndex; i <= sheet.getLastRowNum(); i++) {

            // read row data
            Row row = sheet.getRow(i);
            if (row == null) continue;

            // read cell data
            int age = parseNumericCellValue(row, 0);
            String dogBreedRank = PetType.isDog(petType) ? parseStringCellValue(row, 1) : null;
            int premium = parseNumericCellValue(row, PetType.isDog(petType) ? 7 : 6);

            String renewalCycleString = parseStringCellValue(row, PetType.isDog(petType) ? 2 : 1);
            String coverageRatioString = parseStringCellValue(row, PetType.isDog(petType) ? 3 : 2);
            String deductibleString = parseStringCellValue(row, PetType.isDog(petType) ? 4 : 3);
            String compensationString = parseStringCellValue(row, PetType.isDog(petType) ? 5 : 4);

            // parse enum data
            RenewalCycle renewalCycle = RenewalCycle.getRenewalCycle(renewalCycleString);
            CoverageRatio coverageRatio = CoverageRatio.getCoverageRatio(coverageRatioString);
            Deductible deductible = Deductible.getDeductible(deductibleString);
            Compensation compensation = Compensation.getCompensation(compensationString);

            // save insurance entity
            Insurance insurance = InsuranceConverter.toInsurance(company, petType, age, dogBreedRank, renewalCycle,
                    coverageRatio, deductible, compensation, premium);

            System.out.println(insurance.toString());
            insuranceRepository.save(insurance);
        }
    }

    private void initDogBreedList() {
        Workbook workbook = readExcelFile("assets/SCins_펫보험견적서통합본_240806_전달용.xlsx");
        Sheet sheet = readExcelSheet(workbook, 6);

        for (int i = 2; i <= sheet.getLastRowNum(); i++) {
            // read row data
            Row row = sheet.getRow(i);
            if (row == null) continue;

            // Pet entity 저장
            Pet pet = savePetEntity(row, PetType.DOG);

            // DogBreedDetail entity 저장
            DogBreedDetail dogBreedDetail = saveDogBreedDetailEntity(row, pet);

            // CompanyDogBreed entity 저장
            saveCompanyDogBreedEntities(row, pet);
        }
    }


    private void initCatBreedList() {
    }

    private Pet savePetEntity(Row row, PetType petType) {
        String detailType = parseStringCellValue(row, 1);
        Pet pet = PetConverter.toPet(petType, detailType);
        petRepository.save(pet);
        return pet;
    }

    private DogBreedDetail saveDogBreedDetailEntity(Row row, Pet pet) {
        String isMajorDogBreed = parseStringCellValue(row, 0);
        boolean isFierceDog = parseIsFierceDogCellValue(row); // 맹견여부
        String liabilityCap = parseStringCellValue(row, 13); // 배상책임인수제한

        DogBreedDetail dogBreedDetail = DogBreedConverter.toDogBreedDetail(pet, isFierceDog, liabilityCap, isMajorDogBreed);
        dogBreedDetailRepository.save(dogBreedDetail);
        return dogBreedDetail;
    }

    private void saveCompanyDogBreedEntities(Row row, Pet pet) {
        // company's data column num mapper
        HashMap<Integer, Company> companyDogBreedColumnNumMap = initCompanyDogBreedColumnNumMap();

        for (int c = 2; c <= 10; c+=2) {
            // 회사별 견종 분류
            String dogBreed = parseStringCellValue(row, c);
            if (dogBreed.isEmpty() || dogBreed.contains("-") || dogBreed.equals("비주요견종으로 추후 업데이트")) {
                continue;
            }

            // 회사별 견종 등급
            String dogBreedRank = parseStringCellValue(row, c + 1);
            if (dogBreedRank.contains("-")) {
                dogBreedRank = "";
            }

            CompanyDogBreed companyDogBreed = DogBreedConverter.toCompanyDogBreed(pet, companyDogBreedColumnNumMap.get(c), dogBreed, dogBreedRank);
            companyDogBreedRepository.save(companyDogBreed);
        }
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

    private int parseNumericCellValue(Row row, int cellNum){
        try{
            return (int) row.getCell(cellNum).getNumericCellValue();
        } catch (NullPointerException e){
            return 0;
        }
        catch (Exception e){
            if (cellNum == 6){
                String valueString = row.getCell(6).getStringCellValue();
                if (valueString.equals("가입불가"))
                    return -1;
            }
            else if (cellNum == 7){
                String valueString = row.getCell(7).getStringCellValue();
                if (valueString.equals("인수제한"))
                    return -1;
            }
            return 0;
        }
    }

    private String parseStringCellValue(Row row, int cellNum){
        try {
            return row.getCell(cellNum).getStringCellValue();
        } catch (IllegalStateException e) {
            // 셀이 숫자형 등 다른 타입일 경우 예외 처리
            return String.valueOf((int) row.getCell(cellNum).getNumericCellValue());
        } catch (Exception e) {
            return "";
        }
    }

    private boolean parseIsFierceDogCellValue(Row row){
        try{
            String value = row.getCell(12).getStringCellValue();
            return !value.isEmpty();
        } catch (NullPointerException e){
            return false;
        }
    }

    private HashMap<Integer, Company> initCompanyDogBreedColumnNumMap(){
        return new HashMap<Integer, Company>() {{
            put(2, Company.MERITZ);
            put(4, Company.SAMSUNG);
            put(6, Company.DB);
            put(8, Company.HYUNDAE);
            put(10, Company.KB);
        }};
    }
}
