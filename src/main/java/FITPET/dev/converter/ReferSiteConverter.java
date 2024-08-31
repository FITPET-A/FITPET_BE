package FITPET.dev.converter;

import FITPET.dev.dto.request.ReferSiteRequest;
import FITPET.dev.dto.response.InsuranceResponse;
import FITPET.dev.dto.response.ReferSiteResponse;
import FITPET.dev.dto.response.ReferSiteResponse.ReferSiteDto;
import FITPET.dev.entity.Insurance;
import FITPET.dev.entity.ReferSite;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;

public class ReferSiteConverter {

    public static ReferSiteDto toDto(ReferSite referSite) {
        return ReferSiteDto.builder()
                .refSiteId(referSite.getReferSiteId())
                .channel(referSite.getChannel())
                .url(referSite.getUrl())
                .channelKor(referSite.getChannelKor())
                .build();
    }

    public static List<ReferSiteDto> toDtoList(List<ReferSite> referSites) {
        return referSites.stream()
                .map(ReferSiteConverter::toDto)
                .collect(Collectors.toList());
    }



    public static ReferSite RequestToDto(ReferSiteRequest.ReferSiteDto referSiteDto) {
        return ReferSite.builder()
                .channel(referSiteDto.getChannel())
                .url(referSiteDto.getUrl())
                .channelKor(referSiteDto.getChannelKor())
                .build();
    }

    public static ReferSite updateFromDto(ReferSite referSite, ReferSiteRequest.ReferSiteDto referSiteDto) {
        return ReferSite.builder()
                .referSiteId(referSite.getReferSiteId())
                .channel(referSiteDto.getChannel())
                .url(referSiteDto.getUrl())
                .channelKor(referSiteDto.getChannelKor())
                .build();
    }


    public static ReferSiteResponse.ReferSitePageDto toReferSitePageDto(Page<ReferSite> page) {
        List<ReferSiteResponse.ReferSiteDto> referSiteDtoList = page.getContent().stream()
                .map(ReferSiteConverter::toDto)
                .collect(Collectors.toList());

        return ReferSiteResponse.ReferSitePageDto.builder()
                .content(referSiteDtoList)
                .currentPage(page.getNumber())
                .pageSize(page.getSize())
                .totalNumber(page.getNumberOfElements())
                .totalPage(page.getTotalPages())
                .build();
    }


}
