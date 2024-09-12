package com.example.search.service;

import com.example.common.domain.GeneralResponse;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class SearchServiceImpl implements SearchService {
    @Value("${details-by-city.url}")
    private String detailsByCityUrl;

    @Value("${details-port.url}")
    private String detailsPortUrl;

    private final RestTemplate restTemplate;

    public SearchServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // Call the weather service by city name asynchronously
    @HystrixCommand(fallbackMethod = "fallbackWeatherByCity")
    private CompletableFuture<List<Integer>> getWeatherByCity(String city) {
        return CompletableFuture.supplyAsync(() ->
                restTemplate.getForObject(detailsByCityUrl, List.class, city)
        );
    }

    // Call the weather port service asynchronously
    @HystrixCommand(fallbackMethod = "fallbackWeatherPort")
    private CompletableFuture<String> getWeatherPort() {
        return CompletableFuture.supplyAsync(() ->
                restTemplate.getForObject(detailsPortUrl, String.class)
        );
    }

    // Fallback method for weather by city
    private List<Integer> fallbackWeatherByCity(String city) {
        System.out.println("Fallback for city: " + city);
        return Collections.emptyList();
    }

    // Fallback method for weather port
    private String fallbackWeatherPort() {
        return "Weather port service is unavailable";
    }

    // Merge results from both services and return a GeneralResponse
    public GeneralResponse getMergedDetails(String city) throws InterruptedException, java.util.concurrent.ExecutionException {
        // Fetch the weather by city and the port asynchronously
        CompletableFuture<List<Integer>> weatherByCityFuture = getWeatherByCity(city);
        CompletableFuture<String> weatherPortFuture = getWeatherPort();

        System.out.println("imhere: " + city);

        // Wait for both futures to complete
        List<Integer> weatherByCity = weatherByCityFuture.get();
        String weatherPort = weatherPortFuture.get();

        // Create and return a GeneralResponse with merged results
        GeneralResponse response = new GeneralResponse();
        response.setCode(200);
        response.setTimestamp(new Date());
        response.setData("City IDs: " + weatherByCity + ", Service Port: " + weatherPort);

        return response;
    }
}
