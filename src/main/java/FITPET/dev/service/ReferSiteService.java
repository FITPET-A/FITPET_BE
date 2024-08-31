package FITPET.dev.service;

import FITPET.dev.common.exception.GeneralException;
import FITPET.dev.common.status.ErrorStatus;
import FITPET.dev.converter.InsuranceConverter;
import FITPET.dev.converter.ReferSiteConverter;
import FITPET.dev.dto.request.ReferSiteRequest;
import FITPET.dev.dto.response.InsuranceResponse;
import FITPET.dev.dto.response.ReferSiteResponse;
import FITPET.dev.entity.Insurance;
import FITPET.dev.entity.ReferSite;
import FITPET.dev.repository.ReferSiteRepository;
import FITPET.dev.repository.ReferSiteRepository;
import java.sql.Ref;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReferSiteService {

    private final ReferSiteRepository referSiteRepository;

    /*
     * 신규 유입채널 추가
     */
    public ReferSiteResponse.ReferSiteDto postReferSite(ReferSiteRequest.ReferSiteDto referSiteDto) {

        ReferSite referSite = ReferSiteConverter.RequestToDto(referSiteDto);
        ReferSite savedRefSite = referSiteRepository.save(referSite);
        return ReferSiteConverter.toDto(savedRefSite);

    }

    /*
     * 유입채널 수정
     */
    @Transactional
    public ReferSiteResponse.ReferSiteDto patchReferSite(Long referSiteId, ReferSiteRequest.ReferSiteDto referSiteDto) {
        ReferSite referSite = referSiteRepository.findById(referSiteId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.NOT_EXIST_REFERSITE));
        referSite = ReferSiteConverter.updateFromDto(referSite, referSiteDto);
        ReferSite updatedReferSite = referSiteRepository.save(referSite);
        return ReferSiteConverter.toDto(updatedReferSite);
    }

}
