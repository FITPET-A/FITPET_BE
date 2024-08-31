package FITPET.dev.converter;

import FITPET.dev.dto.request.RefSiteRequest;
import FITPET.dev.dto.response.RefSiteResponse.RefSiteDto;
import FITPET.dev.entity.RefSite;
import java.util.List;
import java.util.stream.Collectors;

public class RefSiteConverter {

    public static RefSiteDto toDto(RefSite refSite) {
        return RefSiteDto.builder()
                .refSiteId(refSite.getRefSiteId())
                .channel(refSite.getChannel())
                .url(refSite.getUrl())
                .channelKor(refSite.getChannelKor())
                .build();
    }

    public static List<RefSiteDto> toDtoList(List<RefSite> refSites) {
        return refSites.stream()
                .map(RefSiteConverter::toDto)
                .collect(Collectors.toList());
    }

    public static RefSite RequestToDto(RefSiteRequest.RefSiteDto refSiteDto) {
        return RefSite.builder()
                .channel(refSiteDto.getChannel())
                .url(refSiteDto.getUrl())
                .channelKor(refSiteDto.getChannelKor())
                .build();
    }

}
