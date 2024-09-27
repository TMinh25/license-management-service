package com.fpt.fis.license_management.jobs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fpt.fis.license_management.model.LicenseModel;
import com.fpt.fis.license_management.repository.entity.License;
import com.fpt.fis.license_management.service.LicenseService;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@DisallowConcurrentExecution
public class LicenseRequest113 extends QuartzJobBean {
    @Value("${job.constants.license-request-113.url}")
    String licenseRequestURL;

    @Autowired
    LicenseService licenseService;

    private static final String STORAGE_DIRECTORY = "C:/local_storage/licenses";

    private void createDirectoryIfNotExists(String directoryPath) throws IOException {
        Path path = Paths.get(directoryPath);
        if (Files.notExists(path)) {
            Files.createDirectories(path);
        }
    }

    // Step 1: Generate the formatted filename
    private String generateFileName() {
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMyyyy");
        String formattedDate = today.format(formatter);
        return "spro_" + formattedDate + ".json";
    }

    private Mono<Void> writeLicensesToFile(Flux<License> licensesFlux, String fileName) {
        return licensesFlux.map(LicenseModel::fromEntity).collectList().flatMap(licenses -> {
            try {
                createDirectoryIfNotExists(STORAGE_DIRECTORY);

                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.registerModule(new JavaTimeModule());

                File file = new File(STORAGE_DIRECTORY, fileName);
                objectMapper.writeValue(file, licenses);
                return Mono.empty();
            } catch (IOException e) {
                return Mono.error(e);
            }
        });
    }

    public Mono<String> sendFileToApi(String fileName) {
        File file = new File(STORAGE_DIRECTORY, fileName);
        FileSystemResource fileResource = new FileSystemResource(file);

        WebClient webClient = WebClient.builder().baseUrl(licenseRequestURL + fileName).build();

        return webClient.post().bodyValue(fileResource).retrieve().bodyToMono(String.class);
    }

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        String fileName = generateFileName();
        writeLicensesToFile(licenseService.readAll(), fileName)
                .then(sendFileToApi(fileName))
                .subscribe(
                        response -> System.out.println("Response from API: " + response),
                        error -> System.err.println("Error: " + error.getMessage())
                );
    }
}
