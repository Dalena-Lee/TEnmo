package com.techelevator.tenmo.validator;

import com.techelevator.tenmo.dao.*;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.security.Principal;

@Component
public class Validator {
    private JdbcTemplate jdbcTemplate;

    public Validator(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    @Autowired
    private JdbcAccountDao accountDao;
    @Autowired
    private JdbcUserDao userDao;
    @Autowired
    private JdbcTransferDao transferDao;

    public boolean hasSufficientFunds(int userId, double amount){
        Account principalAccount = accountDao.getAccountWithUserID(userId);

        if (amount > 0 && principalAccount.getBalance() >= amount) {
            return true;
        } else {
            return false;
        }

    }

    public boolean isNotRequestingLessThanZero(double amountWanted) {
        if (amountWanted > 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isValidStatusID(int statusId){
        if (statusId == 2) {
            return false;
        } else {
            return true;
        }
    }

    public boolean isValidUser(int request_userID, int principal_userID) {
        if (request_userID == principal_userID) {
            return false;
        } else {
            return true;
        }
    }

    public boolean canEditTransferRequest(int userId, int transferId){
        Account principalAccount = accountDao.getAccountWithUserID(userId);
        Transfer currentTransfer = transferDao.getTransferById(transferId);
        
        if (currentTransfer.getSenderID() != principalAccount.getUserID()) {
            return false;
        } else {
            return true;
        }
    }

    public boolean canAccessTransferTransaction(int userId, int transferId){
        Account principalAccount = accountDao.getAccountWithUserID(userId);
        Transfer currentTransfer = transferDao.getTransferById(transferId);

        if((currentTransfer.getSenderID() == principalAccount.getUserID()) || (currentTransfer.getRecipientID() == principalAccount.getUserID())){
            return true;
        } else {
            return false;
        }
    }

    public boolean isPending(int transferId){
        Transfer currentTransfer = transferDao.getTransferById(transferId);

        if (currentTransfer.getStatusId() != 2) {
            return false;
        } else {
            return true;
        }
    }
}
