package com.example.search.service;

import com.example.common.domain.GeneralResponse;
import org.springframework.stereotype.Service;

@Service
public interface SearchService {
    GeneralResponse getMergedDetails(String city);
}
