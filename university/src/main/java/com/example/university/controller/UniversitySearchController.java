package com.example.university.controller;

import com.example.university.pojo.UniversityDTO;
import com.example.university.service.UniversityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "university")
public class UniversitySearchController {
    private final UniversityService universityService;

    @Autowired
    public UniversitySearchController(UniversityService universityService) {
        this.universityService = universityService;
    }

    @GetMapping(params = "countries")
    public ResponseEntity<UniversityDTO[]> getUniversitiesByCountries(@RequestParam("countries") List<String> countries) {
        UniversityDTO[] universities = universityService.getUniversitiesByCountries(countries);
        return ResponseEntity.ok(universities);
    }

    @GetMapping()
    public ResponseEntity<UniversityDTO[]> getAllUniversities() {
        UniversityDTO[] universities = universityService.getAllUniversities();
        return ResponseEntity.ok(universities);
    }
}