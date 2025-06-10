package cri.sw.crypto.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@AllArgsConstructor
public class HighestNormalizedDto {

    @Setter
    @Getter
    String crypto;
}
