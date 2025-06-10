package cri.sw.crypto.services;

import cri.sw.crypto.models.CryptoCsvData;
import jakarta.annotation.PostConstruct;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import java.io.*;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@Service
public class LoadCryptoFilesService {
    private static final Logger log = LoggerFactory.getLogger(LoadCryptoFilesService.class);

    private final Map<String, List<CryptoCsvData>> cryptoData = new HashMap<>();

    public Map<String, List<CryptoCsvData>> getAllCryptoData() {
        return cryptoData;
    }


    @Value("${crypto.data.dir}")
    String relativeFolderPath;

    @PostConstruct
    public void init() throws IOException {
        try {

            List<Resource> resources = getCsvResourcesFromRelativePath(relativeFolderPath);

            for (Resource resource : resources) {
                loadCsv(resource);
            }
        }catch(IOException e){
            log.error(String.valueOf(e));
        }

    }

    public List<Resource> getCsvResourcesFromRelativePath(String relativeFolderPath) {
        File folder = new File(relativeFolderPath); // e.g., "./data"
        File[] files = folder.listFiles((dir, name) -> name.endsWith("_values.csv"));

        List<Resource> resources = new ArrayList<>();
        if (files != null) {
            for (File file : files) {
                resources.add(new FileSystemResource(file));
            }
        }

        return resources;
    }

    private void loadCsv(Resource resource) throws IOException {
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

            for (List<CryptoCsvData> dataList : cryptoData.values()) {
                dataList.sort(Comparator.comparing(CryptoCsvData::getDate));
            }

        } catch (IOException e) {
            log.error(String.valueOf(e));
            throw new IOException();
        }
    }

    public void reloadCsvFile(String fullPath) throws IOException {
        Resource resource = new FileSystemResource(fullPath);
        String symbol = extractSymbolFromFilename(resource.getFilename());

        loadCsv(resource);
    }

    public void removeCsvFile(String filename) {
        String symbol = extractSymbolFromFilename(filename);
        cryptoData.remove(symbol);
    }

    private String extractSymbolFromFilename(String filename) {
        return filename.split("_")[0]; // e.g., BTC from BTC_values.csv
    }

}
