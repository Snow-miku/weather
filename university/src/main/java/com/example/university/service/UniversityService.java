package com.example.university.service;

import com.example.university.pojo.UniversityDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UniversityService {
    UniversityDTO[] getAllUniversities();
    UniversityDTO[] getUniversitiesByCountries(List<String> countries);
}
