package cri.sw.crypto.controllers;
import cri.sw.crypto.dtos.SortedCryptosByNormalizedRangeDto;
import cri.sw.crypto.services.CryptoStatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/cryptos")
public class SortedCryptosByNormalizedRangeController {

    @Autowired
    private CryptoStatsService cryptoStatsService;

    @GetMapping("/normalized")
    public List<SortedCryptosByNormalizedRangeDto> getSortedByNormalizedRange() {
        return cryptoStatsService.getSortedCryptosByNormalizedRange();
    }

}
