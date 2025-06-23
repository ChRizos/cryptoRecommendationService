package cri.sw.crypto.controllers;

import cri.sw.crypto.dtos.HighestNormalizedDto;
import cri.sw.crypto.exceptions.DataNotFoundException;
import cri.sw.crypto.services.CryptoStatsService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/cryptos")
public class HighestNormalizedRangeController {

    @Autowired
    private CryptoStatsService cryptoStatsService;

    @GetMapping("/highest-normalized/{date}")
    @Operation(summary = "Get the crypto with the highest normalized range for a specific day", description = "Returns the crypto with the highest normalized range for a specific day")
    public ResponseEntity<HighestNormalizedDto> getHighestNormalized(@PathVariable String date) {

        LocalDate parsedDate = LocalDate.parse(date);

        try{
              HighestNormalizedDto highestNormalizedDto = cryptoStatsService.getHighestNormalizedCryptoForDate(parsedDate);
              return ResponseEntity.ok(highestNormalizedDto);
        } catch (DataNotFoundException e) {
            throw new DataNotFoundException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

}
