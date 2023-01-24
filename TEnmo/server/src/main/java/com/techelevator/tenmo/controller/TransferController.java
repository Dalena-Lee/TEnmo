package com.techelevator.tenmo.controller;


import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.validator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")
public class TransferController {

    @Autowired
    private TransferDao transferDao;

    @Autowired
    private Validator validator;

    @Autowired
    private UserDao userDao;

    @Autowired
    private AccountDao accountDao;

    public TransferController(TransferDao transferDao){
        this.transferDao = transferDao;
    }

    @RequestMapping(path = "transfers", method = RequestMethod.GET)
    public List<Transfer> list(Principal principal) {
        int userId = userDao.findIdByUsername(principal.getName());
        return this.transferDao.getListOfTransfers(userId);
    }

    @RequestMapping(path = "transfer/{transferId}", method = RequestMethod.GET)
    public Transfer getTransferById(@Valid @PathVariable int transferId, Principal principal){
        int userId = userDao.findIdByUsername(principal.getName());

        if(!validator.canAccessTransferTransaction(userId, transferId)){
            return null;
        } else {
            return this.transferDao.getTransferById(transferId);
        }
    }

    @RequestMapping(path = "transfer", method = RequestMethod.GET)
    public List<Transfer> getFilteredListOfTransfers(@RequestParam int statusId, Principal principal){
        int userId = userDao.findIdByUsername(principal.getName());
        return this.transferDao.filterTransfersByStatus(statusId, userId);
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(path = "transfer/{transferId}", method = RequestMethod.PUT)
        public String updatePendingTransferStatus(@PathVariable int transferId, @RequestParam int statusId, Principal principal) {
        Transfer transferToUpdate = transferDao.getTransferById(transferId);
        int userId = userDao.findIdByUsername(principal.getName());

        if (!validator.canEditTransferRequest(userId, transferId)) {
           return "You are not allowed to approve or reject this transfer request. You must be the one being requested to send money.";
        } else if (!validator.isPending(transferId)) {
            return "You are trying to update a transaction that has already been approved or rejected. Only access pending requests.";
        } else if (!validator.isValidStatusID(statusId)){
            return "Cannot set pending to pending.";
        } else {
            this.transferDao.updateTransferStatus(transferId, statusId);
            return "Status successfully updated. Your new balance is $" + accountDao.getBalanceWithUserID(transferToUpdate.getSenderID());
        }
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(path = "transfer/send", method = RequestMethod.POST)
    public String sendMoney(@Valid @RequestBody Transfer transfer, Principal principal){
        User user = userDao.findByUsername(transfer.getUsername());
        int userId = userDao.findIdByUsername(principal.getName());

        if (user == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User is not in database. Please enter the username of an existing user.");
        } else if (!validator.isNotRequestingLessThanZero(transfer.getAmount())){
            return "Cannot send less than $0. Request failed.";
        } else if (!validator.hasSufficientFunds(userId, transfer.getAmount())) {
            return "Insufficient funds. Send request Failed.";
        } else if (!validator.isValidUser(userDao.findIdByUsername(transfer.getUsername()), userId)) {
            return "Cannot request money from self or send money to self. Request failed.";
        } else {
            this.transferDao.sendMoney(userId, transfer.getUsername(), transfer.getAmount());
            return "Money sent successfully to recipient.";
        }

    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(path = "transfer/request", method = RequestMethod.POST)
    public String requestMoney(@Valid @RequestBody Transfer transfer, Principal principal){
        User user = userDao.findByUsername(transfer.getUsername());
        int userId = userDao.findIdByUsername(principal.getName());

        if (user == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User is not in database. Please enter the username of an existing user.");
        } else if (!validator.isNotRequestingLessThanZero(transfer.getAmount())) {
            return "Cannot request less than $0. Request failed.";
        } else if (!validator.isValidUser(userDao.findIdByUsername(transfer.getUsername()), userId)) {
            return "Cannot request money from self or send money to self. Request failed.";
        } else {
            this.transferDao.requestMoney(transfer.getUsername(), userId, transfer.getAmount());
            return "Request sent successfully.";
        }

    }

}
