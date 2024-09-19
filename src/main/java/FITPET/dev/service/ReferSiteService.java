package FITPET.dev.service;

import FITPET.dev.common.exception.GeneralException;
import FITPET.dev.common.status.ErrorStatus;
import FITPET.dev.converter.ComparisonConverter;
import FITPET.dev.converter.InsuranceConverter;
import FITPET.dev.converter.ReferSiteConverter;
import FITPET.dev.dto.request.ReferSiteRequest;
import FITPET.dev.dto.response.ComparisonResponse;
import FITPET.dev.dto.response.InsuranceResponse;
import FITPET.dev.dto.response.ReferSiteResponse;
import FITPET.dev.entity.Comparison;
import FITPET.dev.entity.Insurance;
import FITPET.dev.entity.ReferSite;
import FITPET.dev.repository.ReferSiteRepository;
import FITPET.dev.repository.ReferSiteRepository;
import java.sql.Ref;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReferSiteService {

    private final ReferSiteRepository referSiteRepository;

    /*
     * 신규 유입채널 추가
     */
    @Transactional
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
        ReferSite referSite = findReferSiteById(referSiteId);
        referSite = ReferSiteConverter.updateFromDto(referSite, referSiteDto);
        ReferSite updatedReferSite = referSiteRepository.save(referSite);
        return ReferSiteConverter.toDto(updatedReferSite);
    }

    /*
     * 유입채널 조회
     */
    public ReferSiteResponse.ReferSitePageDto getReferSite(int page) {
        Pageable pageable = PageRequest.of(page, 20);
        Page<ReferSite> referSitePage = referSiteRepository.findAllNotDeleted(pageable);
        return ReferSiteConverter.toReferSitePageDto(referSitePage);
    }

    /*
     * 유입채널 삭제
     */
    @Transactional
    public void deleteReferSite(Long referSiteId) {
        ReferSite referSite = findReferSiteById(referSiteId);
        referSite.setDeletedAt();
        referSiteRepository.save(referSite);
    }

    /*
     * 유입채널 검색
     * @param content
     */
    public ReferSiteResponse.ReferSitePageDto searchReferSite(String content, int page) {
        int size = 20;
        Pageable pageable = PageRequest.of(page, size);
        Page<ReferSite> referSitePage = referSiteRepository.searchReferSites(content, pageable);

        return ReferSiteConverter.toReferSitePageDto(referSitePage);
    }

    private ReferSite findReferSiteById(Long referSiteId) {
        return referSiteRepository.findByIdAndNotDeleted(referSiteId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.NOT_EXIST_REFERSITE));
    }
}
