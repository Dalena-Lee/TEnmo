package com.techelevator.tenmo.services;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

public class ClientRestService {
    private static final String API_BASE_URL = "http://localhost:8080/";
    RestTemplate restTemplate;

    public ClientRestService(){
        restTemplate = new RestTemplate();
    }

    @Override
    public boolean saveDuckImage(Duck duck) {
        HttpEntity<Duck> entity = makeEntity(duck);
        return (restTemplate.postForObject(API_BASE_URL + "duck", entity, DuckApi.class) != null);
    }

    @Override
    public void deleteDuckImage(int id) {
        restTemplate.delete(API_BASE_URL + id);
    }

    @Override
    public void updateDuckImage(int id, Duck duck) {
        restTemplate.put(API_BASE_URL + id, duck);
    }

    private HttpEntity makeEntity(Duck duck){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Duck> entity = new HttpEntity<>(duck, headers);
        return entity;
    }
}
