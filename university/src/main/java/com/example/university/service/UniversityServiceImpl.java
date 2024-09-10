package com.example.university.service;

import com.example.university.pojo.UniversityDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

@Service
public class UniversityServiceImpl implements UniversityService {

    @Value("${university.url}")
    private String url;
    private final RestTemplate restTemplate;
    private final ExecutorService threadPool;

    public UniversityServiceImpl(RestTemplate restTemplate, ExecutorService threadPool) {
        this.restTemplate = restTemplate;
        this.threadPool = threadPool;
    }

    public UniversityDTO[] getUniversitiesByCountries(List<String> countries) {
        List<CompletableFuture<UniversityDTO[]>> futures = countries.stream()
                .map(country -> CompletableFuture.supplyAsync(
                        () -> restTemplate.getForObject(url + "?country=" + country, UniversityDTO[].class),
                        threadPool))
                .collect(Collectors.toList());

        return futures.stream()
                .map(CompletableFuture::join)
                .flatMap(java.util.Arrays::stream)
                .toArray(UniversityDTO[]::new);
    }

    public UniversityDTO[] getAllUniversities() {
        return restTemplate.getForObject(url, UniversityDTO[].class);
    }
}