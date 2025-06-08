package cri.sw.crypto.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@AllArgsConstructor
public class SortedCryptosByNormalizedRangeDto {
    @Setter
    @Getter
    private String cryptoSymbol;

    @Getter
    @Setter
    private double normalizedValue;

}
