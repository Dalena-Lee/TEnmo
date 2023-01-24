package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;

import java.security.Principal;

public interface AccountDao {

    double getBalanceWithUserID(int user_id);

    void addToBalanceWithID(int account_id, double amount);

    void subtractFromBalanceWithID(int account_id, double amount);

    Account getAccountWithUserID(int user_id);

}
