package cri.sw.crypto.services;

import cri.sw.crypto.dtos.HighestNormalizedDto;
import cri.sw.crypto.dtos.SortedCryptosByNormalizedRangeDto;
import cri.sw.crypto.exceptions.UnsupportedCryptoException;
import cri.sw.crypto.exceptions.DataNotFoundException;
import cri.sw.crypto.models.CryptoCsvData;
import cri.sw.crypto.dtos.StatisticsDto;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class CryptoStatsService {
    private final LoadCryptoFilesService dataLoader;
    private final Map<String, StatisticsDto> statistics = new HashMap<>();

    public CryptoStatsService(LoadCryptoFilesService dataLoader) {
        this.dataLoader = dataLoader;

        calculateStatisticsFromData();
    }

    /**
     * // Calculates the min/max/newest/oldest of all the cryptos on startup or when on file change
     */
    public void calculateStatisticsFromData() {
        Map<String, List<CryptoCsvData>> data = dataLoader.getAllCryptoData();

        statistics.clear();

        for (Map.Entry<String, List<CryptoCsvData>> entry : data.entrySet()) {
            List<CryptoCsvData> dataList = entry.getValue();

            StatisticsDto statisticsDto = new StatisticsDto(
                    dataList.stream().min(Comparator.comparing(CryptoCsvData::getDate)).get().getDate(),
                    dataList.stream().max(Comparator.comparing(CryptoCsvData::getDate)).get().getDate(),
                    dataList.stream().min(Comparator.comparing(CryptoCsvData::getPrice)).get().getPrice(),
                    dataList.stream().max(Comparator.comparing(CryptoCsvData::getPrice)).get().getPrice()
            );

            statistics.put(entry.getKey(), statisticsDto);
        }

    }

    public List<SortedCryptosByNormalizedRangeDto> getSortedCryptosByNormalizedRange(LocalDate startDate, LocalDate endDate) {
        Map<String, List<CryptoCsvData>> data = dataLoader.getAllCryptoData();

        return data.entrySet().stream()
                .map(entry -> {
                    String symbol = entry.getKey();
                    double normalized = getNormalizedRange(entry.getValue(), startDate, endDate);
                    return new SortedCryptosByNormalizedRangeDto(symbol, normalized);
                })
                .sorted(Comparator.comparingDouble(SortedCryptosByNormalizedRangeDto::getNormalizedValue).reversed())
                .toList();
    }


    /**
     * // Returns the min/max/newest/oldest of a specific crypto.
     */
    public StatisticsDto getStats(String symbol, LocalDate startDate, LocalDate endDate) throws UnsupportedCryptoException {

        if (!statistics.containsKey(symbol)) throw new UnsupportedCryptoException(HttpStatus.NOT_FOUND, "Unsupported crypto symbol: " + symbol);

        // Filters the dataset by a startDate and an endDate. If one of those is null it calculates the stats for the whole dataset
        if (startDate != null && endDate != null) {
            Map<String, List<CryptoCsvData>> data = dataLoader.getAllCryptoData();

            List<CryptoCsvData> prices = data.get(symbol);

            prices = prices.stream()
                    .filter(p -> !p.getDate().isBefore(startDate) && !p.getDate().isAfter(endDate))
                    .toList();

            return new StatisticsDto(
                    prices.stream().min(Comparator.comparing(CryptoCsvData::getDate)).get().getDate(),
                    prices.stream().max(Comparator.comparing(CryptoCsvData::getDate)).get().getDate(),
                    prices.stream().min(Comparator.comparing(CryptoCsvData::getPrice)).get().getPrice(),
                    prices.stream().max(Comparator.comparing(CryptoCsvData::getPrice)).get().getPrice()
            );
        }

        return statistics.get(symbol);
    }

    public HighestNormalizedDto getHighestNormalizedCryptoForDate(LocalDate date) throws DataNotFoundException {
        Map<String, List<CryptoCsvData>> data = dataLoader.getAllCryptoData();

        String highestNormalizedCrypto = "";
        double maxNormalized = -1;

        for (Map.Entry<String, List<CryptoCsvData>> entry : data.entrySet()) {
            String crypto = entry.getKey();
            List<CryptoCsvData> filtered = new ArrayList<>();

            for (CryptoCsvData cryptoPrice : entry.getValue()) {
                if (cryptoPrice.getDate().equals(date)) {
                    filtered.add(cryptoPrice);
                }
            }

            double normalized = filtered.isEmpty() ? 0 : getNormalizedRange(filtered, null, null);

            if ((normalized > maxNormalized) && !filtered.isEmpty()) {
                maxNormalized = normalized;
                highestNormalizedCrypto = crypto;
            }
        }

        if(maxNormalized == -1){
            throw new DataNotFoundException(HttpStatus.NOT_FOUND, "No data found for given date");
        }

        return new HighestNormalizedDto(highestNormalizedCrypto);
    }

    private double getNormalizedRange(List<CryptoCsvData> prices, LocalDate startDate, LocalDate endDate) {
        if (startDate != null && endDate != null) {
            prices = prices.stream()
                    .filter(p -> !p.getDate().isBefore(startDate) && !p.getDate().isAfter(endDate))
                    .toList();
        }

        double min = prices.stream().mapToDouble(CryptoCsvData::getPrice).min().orElse(0);
        double max = prices.stream().mapToDouble(CryptoCsvData::getPrice).max().orElse(0);
        return (min == 0) ? 0 : (max - min) / min;
    }
}
