package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.User;

import java.security.Principal;
import java.util.List;
import java.util.Map;

public interface UserDao {

    List<User> findAll();

    List<String> listUsernames(Principal principal);

    User findByUsername(String username);

    int findIdByUsername(String username);

    boolean create(String username, String password);
}
