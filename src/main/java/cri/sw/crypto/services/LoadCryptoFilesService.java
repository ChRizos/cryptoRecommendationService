package cri.sw.crypto.services;

import cri.sw.crypto.entities.CryptoCsvData;
import jakarta.annotation.PostConstruct;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Service;
import java.io.*;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@Service
public class LoadCryptoFilesService {
    private final Map<String, List<CryptoCsvData>> cryptoData = new HashMap<>();

    public Map<String, List<CryptoCsvData>> getAllCryptoData() {
        return cryptoData;
    }

    public List<CryptoCsvData> getDataForSymbol(String symbol) {
        return cryptoData.getOrDefault(symbol.toUpperCase(), Collections.emptyList());
    }


    @PostConstruct
    public void init() throws IOException {
        try {
            PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            Resource[] resources = resolver.getResources("classpath:data/*_values.csv");

            for (Resource resource : resources) {
                loadCsv(resource);
            }
        }catch(IOException e){
            e.printStackTrace();
        }

    }

    private void loadCsv(Resource resource) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()));
             CSVParser parser = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(reader)) {

            for (CSVRecord csvRecord : parser) {
                long timestamp = Long.parseLong(csvRecord.get("timestamp"));
                String symbol = csvRecord.get("symbol");
                double price = Double.parseDouble(csvRecord.get("price"));
                LocalDate date = Instant.ofEpochMilli(timestamp).atZone(ZoneId.systemDefault()).toLocalDate();

                cryptoData.computeIfAbsent(symbol, k -> new ArrayList<>())
                        .add(new CryptoCsvData(date, symbol, price));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
