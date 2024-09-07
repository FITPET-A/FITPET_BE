package FITPET.dev.service;

import FITPET.dev.common.enums.ComparisonStatus;
import FITPET.dev.common.enums.PetType;
import FITPET.dev.common.status.ErrorStatus;
import FITPET.dev.common.exception.GeneralException;
import FITPET.dev.common.utils.ApachePdfUtils;
import FITPET.dev.common.utils.ExcelUtils;
import FITPET.dev.converter.ComparisonConverter;
import FITPET.dev.converter.InsuranceConverter;
import FITPET.dev.converter.PetInfoConverter;
import FITPET.dev.dto.request.ComparisonRequest;
import FITPET.dev.dto.response.ComparisonResponse;
import FITPET.dev.dto.response.InsuranceResponse;
import FITPET.dev.entity.Comparison;
import FITPET.dev.entity.Insurance;
import FITPET.dev.entity.Pet;
import FITPET.dev.entity.PetInfo;
import FITPET.dev.entity.ReferSite;
import FITPET.dev.repository.ComparisonRepository;
import FITPET.dev.repository.PetInfoRepository;
import FITPET.dev.repository.PetRepository;

import FITPET.dev.repository.ReferSiteRepository;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import jakarta.servlet.http.HttpServletResponse;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ComparisonService {

    private final PetInfoRepository petInfoRepository;
    private final ReferSiteRepository refSiteRepository;
    private final PetRepository petRepository;
    private final InsuranceService insuranceService;
    private final ComparisonRepository comparisonRepository;
    private final ExcelUtils excelUtils;
    private final ApachePdfUtils apachePdfUtils;

    // 최소, 최대 조회 기간
    private final LocalDateTime minDateTime = LocalDateTime.of(2000, 1, 1, 0, 0);
    private final LocalDateTime maxDateTime = LocalDateTime.of(3999, 12, 31, 23, 59, 59);
    private static final Pattern PHONE_NUMBER_PATTERN = Pattern.compile("^010-\\d{4}-\\d{4}$");
    static final int MAX_AGE = 10;


    /*
     * PetInfo 생성 및 저장
     * @param request
     * @return
     */
    public ComparisonResponse.ComparisonDto createComparisonAndGetInsurance(ComparisonRequest request) {
        validatePhoneNumber(request.getPhoneNumber());
        validateAge(request.getPetAge());
        Pet pet = findPetByPetSpecies(request.getPetSpecies());

        // pet의 품종과 request의 품종이 다를 시 exception 반환
        if (pet.getPetType() != request.getPetType())
            throw new GeneralException(ErrorStatus.INVALID_PETTYPE_WITH_DETAILTYPE);

        // PetInfo 생성하고 저장
        PetInfo petInfo = PetInfoConverter.toPetInfo(request, pet);
        petInfo = petInfoRepository.save(petInfo);

        ReferSite referSite = null;
        if (request.getReferSite() != null)
            referSite = findReferSiteByChannel(request.getReferSite());

        Comparison comparison = ComparisonConverter.toComparison(petInfo, referSite, request.getReferUserId(), request.getComment());
        comparisonRepository.save(comparison);
        return ComparisonConverter.toComparisonDto(comparison);
    }


    /*
     * default 값으로 보험료 조회
     * @param petInfo
     * @return
     */
    public InsuranceResponse.InsuranceListDto getInsurancePremiumsByPetInfo(PetInfo petInfo) {

        String detailType = petInfo.getPet().getPetSpecies();
        int age = petInfo.getAge();
        String renewalCycle = "3년";
        String deductible = "1만원";
        String coverageRatio = "70";
        String compensation = "15만";

        return insuranceService.getInsurancePremium(detailType, age, renewalCycle, coverageRatio, deductible, compensation);
    }


    public InsuranceResponse.InsuranceListDto getInsurancePremiumsByPetInfoAndCoverageRatio(PetInfo petInfo, String coverageRatio) {

        String detailType = petInfo.getPet().getPetSpecies();
        int age = petInfo.getAge();
        String renewalCycle = "3년";
        String deductible = "1만원";
        String compensation = "15만";

        return insuranceService.getInsurancePremium(detailType, age, renewalCycle, coverageRatio, deductible, compensation);
    }


    /*
     * 견적 요청 내역 조회 (최신순)
     * @param startDate
     * @param endDate
     * @param page
     * @param comparisonStatus
     * @return
     */
    public ComparisonResponse.ComparisonPageDto getComparisons(String startDate, String endDate, int page, ComparisonStatus comparisonStatus) {

        // 날짜 형식 변경
        LocalDateTime start = parseDate(startDate, " 00:00:00");
        LocalDateTime end = parseDate(endDate, " 23:59:59");

        // 견적 요청 리스트를 페이지 단위로 조회
        Pageable pageable = PageRequest.of(page, 20);
        Page<Comparison> comparisonPage = comparisonRepository.findByCreatedAtBetweenAndStatus(start, end, comparisonStatus, pageable);

        return ComparisonConverter.toComparisonPageDto(comparisonPage);
    }


    /*
     * 견적 요청 내역 엑셀 다운로드
     * @param servletResponse
     */
    public void downloadComparisons(HttpServletResponse servletResponse,
                                    String startDate, String endDate, ComparisonStatus comparisonStatus) {
        // 날짜 형식 변경
        LocalDateTime start = (startDate != null) ? parseDate(startDate, " 00:00:00") : minDateTime;
        LocalDateTime end = (endDate != null) ? parseDate(endDate, " 23:59:59") : maxDateTime;

        // 견적서 요청 리스트 조회
        List<Comparison> comparisonList = comparisonRepository.findByCreatedAtBetweenAndStatus(start, end, comparisonStatus);

        // excelDto로 타입 변경
        List<ComparisonResponse.ComparisonExcelDto> comparisonExcelDtoList = convertToComparisonExcelDtoList(comparisonList);

        // excel 파일 다운로드
        excelUtils.downloadComparisons(servletResponse, comparisonExcelDtoList);
    }

    /*
     * 견적서 생성 및 pdf 다운로드
     * @param servletResponse
     * @param comparisonId
     */
    public void downloadComparisonPdf(HttpServletResponse servletResponse, Long comparisonId) {

        Comparison comparison = findComparisonById(comparisonId);
        PetInfo petInfo = comparison.getPetInfo();
        List<InsuranceResponse.InsuranceDto> seventyInsuranceList = getInsurancePremiumsByPetInfoAndCoverageRatio(petInfo, "70%").getInsuranceDtoList();
        List<InsuranceResponse.InsuranceDto> eightyInsuranceList = getInsurancePremiumsByPetInfoAndCoverageRatio(petInfo, "80%").getInsuranceDtoList();
        List<InsuranceResponse.InsuranceDto> ninetyInsuranceList = getInsurancePremiumsByPetInfoAndCoverageRatio(petInfo, "90%").getInsuranceDtoList();

        InsuranceResponse.AllInsuranceListDto allInsuranceListDto = InsuranceConverter.toAllInsuranceListDto(seventyInsuranceList, eightyInsuranceList, ninetyInsuranceList);
        apachePdfUtils.downloadComparisonPdf(servletResponse, comparison, allInsuranceListDto);
    }


    /*
     * 견적 요청 상태 변경
     * @param comparisonId
     * @param comparisonStatus
     * @return
     */
    @Transactional
    public ComparisonResponse.ComparisonDto patchComparisonStatus(Long comparisonId, ComparisonStatus comparisonStatus){

        // 견적서 단일 조회
        Comparison comparison = findComparisonById(comparisonId);

        // validate status
        ComparisonStatus currentComparisonStatus = comparison.getStatus();
        if (currentComparisonStatus.getIndex() > comparisonStatus.getIndex())
            throw new GeneralException(ErrorStatus.INVALID_PATCH_PERIOR_STATUS);

        // patch status
        comparison.updateStatus(comparisonStatus);
        return ComparisonConverter.toComparisonDto(comparison);
    }


    /*
     * 전화번호와 펫 이름으로 Comparison 검색
     * @param content
     */
    public ComparisonResponse.ComparisonPageDto searchComparisons(String content, int page) {
        int size = 20;
        Pageable pageable = PageRequest.of(page, size);

        // '-' 제거
        String changedContent = content != null ? content.replaceAll("-", "") : null;
        Page<Comparison> comparisonPage = comparisonRepository.findAllByPhoneNumOrPetName(changedContent, pageable);
        return ComparisonConverter.toComparisonPageDto(comparisonPage);
    }

    /*
     * ComparisonId들 받아서 삭제
     * @param ComparisonId
     */

    @Transactional
    public void deleteComparison(Long comparisonId) {
        Comparison comparison = findComparisonById(comparisonId);
        comparison.setDeletedAt();
    }



    /*
     * redirect url 생성
     * @param params
     * @return
     * @throws UnsupportedEncodingException
     * @throws MalformedURLException
     */
    public String makeRedirectUrl(Map<String, String> params) throws UnsupportedEncodingException, MalformedURLException {

        // TODO: 배포 후 domain 수정
        String endpoint = "http://localhost:8080/api/v1/comparison/form";
        StringBuilder urlBuilder = new StringBuilder(endpoint);
        urlBuilder.append("?");

        for (Map.Entry<String, String> entry : params.entrySet()) {
            urlBuilder.append(URLEncoder.encode(entry.getKey(), "UTF-8"))
                    .append("=")
                    .append(URLEncoder.encode(entry.getValue(), "UTF-8"))
                    .append("&");
        }

        URL url = new URL(urlBuilder.toString());
        return url.toString();
    }


    private Comparison findComparisonById(Long comparisonId) {
        return comparisonRepository.findById(comparisonId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.NOT_EXIST_COMPARISON));
    }

    private Pet findPetByPetSpecies(String petSpecies) {
        return petRepository.findByPetSpecies(petSpecies)
                .orElseThrow(() -> new GeneralException(ErrorStatus.NOT_EXIST_PET));
    }

    private ReferSite findReferSiteByChannel(String referSite){
        return refSiteRepository.findByChannel(referSite)
                .orElseThrow(() -> new GeneralException(ErrorStatus.NOT_EXIST_REFERSITE));
    }

    private void validatePhoneNumber(String phoneNum) {
        if (!PHONE_NUMBER_PATTERN.matcher(phoneNum).matches()) {
            throw new GeneralException(ErrorStatus.INVALID_PHONE_NUMBER);
        }
    }

    private void validateAge(int age) {
        if (age < 0) {
            throw new GeneralException(ErrorStatus.INVALID_AGE);
        } else if (age > MAX_AGE) {
            throw new GeneralException(ErrorStatus.INVALID_AGE_OVER_10);
        }
    }

    private LocalDateTime parseDate(String date, String timeSuffix) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        try {
            return LocalDateTime.parse(date + timeSuffix, formatter);
        } catch (DateTimeParseException e) {
            throw new GeneralException(ErrorStatus.INVALID_DATE_FORMAT);
        }
    }

    private List<ComparisonResponse.ComparisonExcelDto> convertToComparisonExcelDtoList(
            List<Comparison> comparisonList) {
        return comparisonList.stream()
                .map(ComparisonConverter::toComparisonExcelDto)
                .toList();
    }


}
