package cri.sw.crypto.controllers;

import cri.sw.crypto.services.CryptoStatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/cryptos")
public class HighestNormalizedRangeController {

    @Autowired
    private CryptoStatsService cryptoStatsService;

    @GetMapping("/highest-normalized/{date}")
    public String getHighestNormalized(@PathVariable String date) {
        LocalDate parsedDate = LocalDate.parse(date);
        return cryptoStatsService.getHighestNormalizedCryptoForDate(parsedDate);
    }

}
