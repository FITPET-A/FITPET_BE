package FITPET.dev.service;

import FITPET.dev.common.status.ErrorStatus;
import FITPET.dev.common.exception.GeneralException;
import FITPET.dev.converter.ComparisonConverter;
import FITPET.dev.converter.PetInfoConverter;
import FITPET.dev.dto.request.ComparisonRequest;
import FITPET.dev.dto.response.InsuranceResponse;
import FITPET.dev.entity.Comparison;
import FITPET.dev.entity.Pet;
import FITPET.dev.entity.PetInfo;
import FITPET.dev.repository.ComparisonRepository;
import FITPET.dev.repository.PetInfoRepository;
import FITPET.dev.repository.PetRepository;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ComparisonService {

    private final PetInfoRepository petInfoRepository;
    private final PetRepository petRepository;
    private final InsuranceService insuranceService;
    private final ComparisonRepository comparisonRepository;

    private static final Pattern PHONE_NUMBER_PATTERN = Pattern.compile("^010-\\d{4}-\\d{4}$");
    static final int MAX_AGE = 10;


    // PetInfo 생성 및 저장 후 보험료 조회
    public InsuranceResponse.InsuranceListDto createComparisonAndGetInsurance(ComparisonRequest request) {
        validatePhoneNumber(request.getPhoneNumber());
        validateAge(request.getPetAge());
        Pet pet = findPetByPetSpecies(request.getPetSpecies());

        // pet의 품종과 request의 품종이 다를 시 exception 반환
        if (pet.getPetType() != request.getPetType())
            throw new GeneralException(ErrorStatus.INVALID_PETTYPE_WITH_DETAILTYPE);

        // PetInfo 생성하고 저장
        PetInfo petInfo = PetInfoConverter.toPetInfo(request, pet);
        petInfo = petInfoRepository.save(petInfo);

        Comparison comparison = ComparisonConverter.toComparison(petInfo, request.getReferSite(), request.getReferUserId(), request.getComment());
        comparison = comparisonRepository.save(comparison);

        // 보험료 조회
        return getInsurancePremiumsByPetInfo(petInfo);
    }


    // default 값으로 보험료 조회
    public InsuranceResponse.InsuranceListDto getInsurancePremiumsByPetInfo(PetInfo petInfo) {

        String detailType = petInfo.getPet().getPetSpecies();
        int age = petInfo.getAge();
        String renewalCycle = "3년";
        String deductible = "1만원";
        String coverageRatio = "70";
        String compensation = "15만";

        return insuranceService.getInsurancePremium(detailType, age, renewalCycle, coverageRatio, deductible, compensation);
    }


    // petSpecies을 기반으로 Pet 조회
    private Pet findPetByPetSpecies(String petSpecies) {
        return petRepository.findByPetSpecies(petSpecies)
                .orElseThrow(() -> new GeneralException(ErrorStatus.NOT_EXIST_PET));
    }

    // 전화번호 유효성 검사
    private void validatePhoneNumber(String phoneNum) {
        if (!PHONE_NUMBER_PATTERN.matcher(phoneNum).matches()) {
            throw new GeneralException(ErrorStatus.INVALID_PHONE_NUMBER);
        }
    }

    // 나이 유효성 검사
    private void validateAge(int age) {
        if (age < 0) {
            throw new GeneralException(ErrorStatus.INVALID_AGE);
        } else if (age > MAX_AGE) {
            throw new GeneralException(ErrorStatus.INVALID_AGE_OVER_10);
        }
    }


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
}
