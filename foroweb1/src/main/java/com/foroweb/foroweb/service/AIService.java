package com.foroweb.foroweb.service;

import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class AIService {
    private final RestTemplate restTemplate;

    public AIService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String analyzePosts(Long studentId) {
        String url = "http://localhost:5000/analyze";
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("student_id", studentId);
        ResponseEntity<String> response = restTemplate.getForEntity(builder.toUriString(), String.class);
        return response.getBody();
    }
    public ResponseEntity<String> profesores(){
        String url = "http://localhost:5000/professor_trend";
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
        ResponseEntity<String> response = restTemplate.getForEntity(builder.toUriString(), String.class);
        return response;
    }
    public ResponseEntity<String> materias(Long studentId){
        String url = "http://localhost:5000/trend_analysis";
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("student_id", studentId);
        ResponseEntity<String> response = restTemplate.getForEntity(builder.toUriString(), String.class);
        return response;
    }
}