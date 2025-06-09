package cri.sw.crypto.services;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@RequiredArgsConstructor
public class FileWatchingService {

    private static final Logger log = LoggerFactory.getLogger(FileWatchingService.class);
    private final LoadCryptoFilesService dataLoader;

    @Value("${crypto.data.dir}")
    private String dataDir;

    private WatchService watchService;
    private ExecutorService executor;

    @PostConstruct
    public void startWatching() {
        executor = Executors.newSingleThreadExecutor();
        try {
            watchService = FileSystems.getDefault().newWatchService();
            Path path = Paths.get(dataDir);
            path.register(watchService,
                    StandardWatchEventKinds.ENTRY_CREATE,
                    StandardWatchEventKinds.ENTRY_MODIFY,
                    StandardWatchEventKinds.ENTRY_DELETE);

            executor.submit(() -> {
                while (!Thread.currentThread().isInterrupted()) {
                    try {
                        WatchKey key = watchService.take(); // blocking call
                        for (WatchEvent<?> event : key.pollEvents()) {
                            WatchEvent.Kind<?> kind = event.kind();
                            Path filename = (Path) event.context();

                            if (filename.toString().endsWith("_values.csv")) {
                                String fullPath = path.resolve(filename).toString();
                                System.out.printf("[FileWatcher] Detected %s -> %s%n", kind.name(), fullPath);

                                if (kind == StandardWatchEventKinds.ENTRY_CREATE ||
                                        kind == StandardWatchEventKinds.ENTRY_MODIFY) {
                                    dataLoader.reloadCsvFile(fullPath);
                                } else if (kind == StandardWatchEventKinds.ENTRY_DELETE) {
                                    dataLoader.removeCsvFile(filename.toString());
                                }
                            }
                        }
                        key.reset();
                    } catch (InterruptedException e) {
                        log.error(String.valueOf(e));
                        Thread.currentThread().interrupt(); // required for clean shutdown
                    } catch (IOException e) {
                        log.error(String.valueOf(e));
                        throw new RuntimeException(e);
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @PreDestroy
    public void stopWatching() throws IOException {
        if (watchService != null) watchService.close();
        if (executor != null) executor.shutdownNow();
    }
}
