package cri.sw.crypto.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
public class CryptoCsvData {

    @Setter
    @Getter
    private LocalDate date;

    @Setter
    @Getter
    private String cryptoSymbol;

    @Setter
    @Getter
    private double price;
}

