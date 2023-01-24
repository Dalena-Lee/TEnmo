package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.security.Principal;
import java.util.List;

public interface TransferDao {

    List<Transfer> getListOfTransfers(int userId);

    Transfer getTransferById(int transferID);

    List<Transfer> filterTransfersByStatus(int status_id, int userId);

    void updateTransferStatus(int transferId, int statusId);

    void sendMoney(int userId, String username, double amountToSend);

    void requestMoney(String username, int userId, double amountWanted);

}
