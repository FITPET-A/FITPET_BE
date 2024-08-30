package FITPET.dev.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

public class PetResponse {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PetSpeciesListDto {
        private List<String> petSpeciesList;
    }
}
