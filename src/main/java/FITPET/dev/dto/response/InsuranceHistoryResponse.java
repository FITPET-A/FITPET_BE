package FITPET.dev.dto.response;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class InsuranceHistoryResponse {
    private Long insuranceHistoryId;
    private int oldValue;
    private int newValue;
    private String updatedAt;
}