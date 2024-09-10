package com.example.university.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UniversityDTO {
    private String name;
    private String country;

    @JsonProperty("state-province")
    private String stateProvince;
    private String[] domains;

    @JsonProperty("web_pages")
    private String[] webPages;

    @JsonProperty("alpha_two_code")
    private String alphaTwoCode;

    public UniversityDTO() {
    }

    public UniversityDTO(String name, String country, String stateProvince, String[] domains, String[] webPages, String alphaTwoCode) {
        this.name = name;
        this.country = country;
        this.stateProvince = stateProvince;
        this.domains = domains;
        this.webPages = webPages;
        this.alphaTwoCode = alphaTwoCode;
    }

    // Getters and Setters
    public String getAlphaTwoCode() {
        return alphaTwoCode;
    }

    public void setAlphaTwoCode(String alphaTwoCode) {
        this.alphaTwoCode = alphaTwoCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getStateProvince() {
        return stateProvince;
    }

    public void setStateProvince(String stateProvince) {
        this.stateProvince = stateProvince;
    }

    public String[] getDomains() {
        return domains;
    }

    public void setDomains(String[] domains) {
        this.domains = domains;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String[] getWebPages() {
        return webPages;
    }

    public void setWebPages(String[] webPages) {
        this.webPages = webPages;
    }
}
