package cri.sw.crypto.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Builder
@AllArgsConstructor
public class StatisticsDto {
    @Setter
    @Getter
    private LocalDate oldest;

    @Setter
    @Getter
    private LocalDate newest;

    @Setter
    @Getter
    private double min;

    @Setter
    @Getter
    private double max;
}
