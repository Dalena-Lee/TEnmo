package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.AuthenticatedUser;
import org.apiguardian.api.API;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

public class AccountService {
    private String API_BASE_URL;
    private RestTemplate restTemplate = new RestTemplate();
    private AuthenticatedUser user;

    public AccountService(String API_BASE_URL, AuthenticatedUser user) {
        this.API_BASE_URL = API_BASE_URL;
        this.user = user;
    }

    public double getBalance() {
        double balance;
        balance = restTemplate.exchange(API_BASE_URL + "/balance", HttpMethod.GET, makeAuthEntity(), Double.class).getBody();
        System.out.println("Your balance is: $" + balance);
        return balance;
    }

    private HttpEntity makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(user.getToken());
        HttpEntity entity = new HttpEntity<>(headers);
        return entity;
    }
}
