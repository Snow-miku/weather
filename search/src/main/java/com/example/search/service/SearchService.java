package com.example.search.service;

import com.example.common.domain.GeneralResponse;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Service
public interface SearchService {
    GeneralResponse getMergedDetails(String city) throws InterruptedException, ExecutionException;
}
