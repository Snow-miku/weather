package com.example.search.controller;

import com.example.search.service.SearchService;
import com.example.common.domain.GeneralResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.concurrent.ExecutionException;

@RestController
public class SearchController {
    private final SearchService searchService;

    @Autowired
    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @GetMapping("/weather/search")
    public ResponseEntity<?> getDetails() {
        try {
            GeneralResponse response = searchService.getMergedDetails("seattle");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (InterruptedException | ExecutionException e) {
            return new ResponseEntity<>("Error fetching data", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}