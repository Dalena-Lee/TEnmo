package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.dao.JdbcUserDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@PreAuthorize("isAuthenticated()")
@RestController
public class AccountController {

    private JdbcAccountDao jdbcAccountDao;
    private JdbcUserDao jdbcUserDao;

    public AccountController(JdbcAccountDao jdbcAccountDao, JdbcUserDao jdbcUserDao) {
        this.jdbcAccountDao = jdbcAccountDao;
        this.jdbcUserDao = jdbcUserDao;
    }

    @RequestMapping(path = "/balance", method = RequestMethod.GET)
    public double getBalance(Principal principal){
        return this.jdbcAccountDao.getBalanceWithUserID(jdbcUserDao.findIdByUsername(principal.getName()));
    }

    @RequestMapping(path = "/user", method = RequestMethod.GET)
    public List<String> listUsernames(Principal principal) {
        return this.jdbcUserDao.listUsernames(principal);
    }

}
