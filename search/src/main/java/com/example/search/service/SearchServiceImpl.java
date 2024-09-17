package com.example.search.service;

import com.example.common.domain.GeneralResponse;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.ExecutionException;
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

    // Asynchronously call /details endpoint
    @HystrixCommand(fallbackMethod = "fallbackGetDetailsByCity")
    private CompletableFuture<List<Integer>> getDetailsByCity(String city) {
        return CompletableFuture.supplyAsync(() -> restTemplate.getForObject(detailsByCityUrl, List.class, city));
    }

    // Asynchronously call /details/port endpoint
    @HystrixCommand(fallbackMethod = "fallbackGetDetailsPort")
    private CompletableFuture<String> getDetailsPort() {
        return CompletableFuture.supplyAsync(() -> restTemplate.getForObject(detailsPortUrl, String.class));
    }

    // Fallback method for getDetailsByCity
    private CompletableFuture<List<Integer>> fallbackGetDetailsByCity(String city, Throwable throwable) {
        System.out.println("Fallback for getDetailsByCity: " + throwable.getMessage());
        return CompletableFuture.completedFuture(null);
    }

    // Fallback method for getDetailsPort
    private CompletableFuture<String> fallbackGetDetailsPort(Throwable throwable) {
        System.out.println("Fallback for getDetailsPort: " + throwable.getMessage());
        return CompletableFuture.completedFuture("Details service is unavailable");
    }

    @Override
    public GeneralResponse getMergedDetails(String city) {
        try {
            // Start both asynchronous calls
            CompletableFuture<List<Integer>> detailsFuture = getDetailsByCity(city);
            CompletableFuture<String> portFuture = getDetailsPort();

            // Wait for both futures to complete
            CompletableFuture.allOf(detailsFuture, portFuture).join();

            // Retrieve the results
            List<Integer> cityIds = detailsFuture.get();
            String detailsPort = portFuture.get();

            // Merge results into GeneralResponse
            GeneralResponse response = new GeneralResponse();
            response.setCode(200);
            response.setTimestamp(new Date());
            response.setData("City IDs: " + cityIds + ", Details Service Port: " + detailsPort);

            return response;
        } catch (InterruptedException | ExecutionException e) {
            // Handle exceptions and return a fallback response
            GeneralResponse response = new GeneralResponse();
            response.setCode(500);
            response.setTimestamp(new Date());
            response.setData("Error fetching data: " + e.getMessage());
            return response;
        }
    }
}
