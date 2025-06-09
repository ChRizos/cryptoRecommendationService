package cri.sw.crypto.controllers;

import cri.sw.crypto.dtos.StatisticsDto;
import cri.sw.crypto.exceptions.UnsupportedCryptoException;
import cri.sw.crypto.services.CryptoStatsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cryptos")
@Tag(name = "Crypto statistics")
public class CryptoStatsController {

    @Autowired
    private CryptoStatsService cryptoStatsService;

    @GetMapping("/{symbol}/stats")
    @Operation(summary = "Get cryptos statistics", description = "Returns the oldest/newest/min/max values for a requested crypto")
    public StatisticsDto getStats(@PathVariable String symbol) {

        try{
            return cryptoStatsService.getStats(symbol.toUpperCase());
        } catch (UnsupportedCryptoException e) {
            throw new UnsupportedCryptoException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}
