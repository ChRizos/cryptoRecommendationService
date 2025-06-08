package cri.sw.crypto.services;

import cri.sw.crypto.dtos.SortedCryptosByNormalizedRangeDto;
import cri.sw.crypto.entities.CryptoCsvData;
import cri.sw.crypto.dtos.StatisticsDto;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class CryptoStatsService {
    private final LoadCryptoFilesService dataLoader;

    public CryptoStatsService(LoadCryptoFilesService dataLoader) {
        this.dataLoader = dataLoader;
    }

//    public List<String> getSortedCryptosByNormalizedRange() {
//        Map<String, List<CryptoPrice>> data = dataLoader.getAllCryptoData();
//
//        return data.entrySet().stream()
//                .map(e -> Map.entry(e.getKey(), getNormalizedRange(e.getValue())))
//                .sorted((a, b) -> Double.compare(b.getValue(), a.getValue()))
//                .map(Map.Entry::getKey)
//                .toList();
//    }

    public List<SortedCryptosByNormalizedRangeDto> getSortedCryptosByNormalizedRange() {
        Map<String, List<CryptoCsvData>> data = dataLoader.getAllCryptoData();

        return data.entrySet().stream()
                .map(entry -> {
                    String symbol = entry.getKey();
                    double normalized = getNormalizedRange(entry.getValue());
                    return new SortedCryptosByNormalizedRangeDto(symbol, normalized);
                })
                .sorted(Comparator.comparingDouble(SortedCryptosByNormalizedRangeDto::getNormalizedValue).reversed())
                .toList();
    }

    public StatisticsDto getStats(String symbol) {
        Map<String, List<CryptoCsvData>> data = dataLoader.getAllCryptoData();

        List<CryptoCsvData> prices = data.get(symbol);
        if (prices == null || prices.isEmpty()) throw new RuntimeException("Crypto not found");

        return new StatisticsDto(
                prices.stream().min(Comparator.comparing(CryptoCsvData::getDate)).get().getDate(),
                prices.stream().max(Comparator.comparing(CryptoCsvData::getDate)).get().getDate(),
                prices.stream().min(Comparator.comparing(CryptoCsvData::getPrice)).get().getPrice(),
                prices.stream().max(Comparator.comparing(CryptoCsvData::getPrice)).get().getPrice()
        );
    }

    public String getHighestNormalizedCryptoForDate(LocalDate date) {
        Map<String, List<CryptoCsvData>> data = dataLoader.getAllCryptoData();

        return data.entrySet().stream()
                .map(e -> {
                    List<CryptoCsvData> filtered = e.getValue().stream()
                            .filter(p -> p.getDate().equals(date))
                            .toList();
                    if (filtered.isEmpty()) return Map.entry(e.getKey(), 0.0);
                    return Map.entry(e.getKey(), getNormalizedRange(filtered));
                })
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("No data");
    }

    private double getNormalizedRange(List<CryptoCsvData> prices) {
        double min = prices.stream().mapToDouble(CryptoCsvData::getPrice).min().orElse(0);
        double max = prices.stream().mapToDouble(CryptoCsvData::getPrice).max().orElse(0);
        return (min == 0) ? 0 : (max - min) / min;
    }
}
