package cri.sw.crypto.controllers;

import cri.sw.crypto.dtos.StatisticsDto;
import cri.sw.crypto.services.CryptoStatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cryptos")
public class CryptoStatsController {

    @Autowired
    private CryptoStatsService cryptoStatsService;

    @GetMapping("/{symbol}/stats")
    public StatisticsDto getStats(@PathVariable String symbol) {
        return cryptoStatsService.getStats(symbol.toUpperCase());
    }
}
