package cri.sw.crypto.controllers;
import cri.sw.crypto.dtos.SortedCryptosByNormalizedRangeDto;
import cri.sw.crypto.services.CryptoStatsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/cryptos")
@Tag(name = "Sorted list of Cryptos by normalized range")
public class SortedCryptosByNormalizedRangeController {

    @Autowired
    private CryptoStatsService cryptoStatsService;

    @GetMapping("/normalized")
    @Operation(summary = "Get cryptos sorted by normalized range", description = "Returns a descending list of cryptos based on their normalized range for the dataset")
    public ResponseEntity<List<SortedCryptosByNormalizedRangeDto>> getSortedByNormalizedRange(@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                                     @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        //fixme: it is not good practice to return a list of dtos for maintainability
         List<SortedCryptosByNormalizedRangeDto> sortedCryptosByNormalizedRangeDto = cryptoStatsService.getSortedCryptosByNormalizedRange(startDate, endDate);
        return  ResponseEntity.ok(sortedCryptosByNormalizedRangeDto);
    }

}
