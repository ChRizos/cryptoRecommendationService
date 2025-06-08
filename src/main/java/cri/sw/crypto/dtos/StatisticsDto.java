package cri.sw.crypto.dtos;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

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

    public StatisticsDto(LocalDate date, LocalDate date1, double min, double max) {
        this.oldest = date;
        this.newest = date1;
        this.min = min;
        this.max = max;
    }
}
