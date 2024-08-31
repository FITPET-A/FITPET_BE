package FITPET.dev.service;

import FITPET.dev.converter.ReferSiteConverter;
import FITPET.dev.dto.request.ReferSiteRequest;
import FITPET.dev.dto.response.ReferSiteResponse;
import FITPET.dev.entity.ReferSite;
import FITPET.dev.repository.ReferSiteRepository;
import FITPET.dev.repository.ReferSiteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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


}
