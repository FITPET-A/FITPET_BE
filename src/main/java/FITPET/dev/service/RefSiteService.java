package FITPET.dev.service;

import FITPET.dev.converter.ProposalConverter;
import FITPET.dev.converter.RefSiteConverter;
import FITPET.dev.dto.request.ProposalRequest;
import FITPET.dev.dto.request.RefSiteRequest;
import FITPET.dev.dto.response.RefSiteResponse;
import FITPET.dev.entity.Proposal;
import FITPET.dev.entity.RefSite;
import FITPET.dev.repository.RefSiteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefSiteService {

    private final RefSiteRepository refSiteRepository;

    /*
     * 신규 유입채널 추가
     */
    public RefSiteResponse.RefSiteDto postRefSite(RefSiteRequest.RefSiteDto refSiteDto) {

        RefSite refSite = RefSiteConverter.RequestToDto(refSiteDto);
        RefSite savedRefSite = refSiteRepository.save(refSite);
        return RefSiteConverter.toDto(savedRefSite);

    }

}
