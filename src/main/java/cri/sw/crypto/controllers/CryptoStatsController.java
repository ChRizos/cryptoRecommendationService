package cri.sw.crypto.controllers;

import cri.sw.crypto.dtos.StatisticsDto;
import cri.sw.crypto.services.CryptoStatsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cryptos")
@Tag(name = "Crypto statistics")
public class CryptoStatsController {

    @Autowired
    private CryptoStatsService cryptoStatsService;

    @GetMapping("/{symbol}/stats")
    @Operation(summary = "Get cryptos statistics", description = "Returns the oldest/newest/min/max values for a requested crypto")
    public StatisticsDto getStats(@PathVariable String symbol) {
        return cryptoStatsService.getStats(symbol.toUpperCase());
    }
}
