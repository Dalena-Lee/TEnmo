package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.validator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
@Component
public class JdbcTransferDao implements TransferDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    private AccountDao accountDao;
    @Autowired
    private UserDao userDao;

    @Autowired
    private Validator validator;

    public JdbcTransferDao(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Transfer> getListOfTransfers(int userID) {
        String sql = "SELECT * " +
                "FROM transfer WHERE (sender_id = ?) OR (recipient_id = ?);";
        List<Transfer> allTransactions = new ArrayList<>();
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, userID, userID);

        while(rowSet.next()){
            Transfer oneTransfer = mapTransferFromRowSet(rowSet);
            allTransactions.add(oneTransfer);
        }

        return allTransactions;
    }

    @Override
    public Transfer getTransferById(int transferID) {

        String sql = "SELECT * FROM transfer WHERE transfer_id = ?;";

        Transfer retrievedTransfer = new Transfer();

        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, transferID);

        if(rowSet.next()){
            retrievedTransfer = mapTransferFromRowSet(rowSet);
            return retrievedTransfer;
        }

        return retrievedTransfer;
    }

    @Override
    public List<Transfer> filterTransfersByStatus(int status_id, int userId) {
        String sql = "SELECT * from transfer where status_id = ? AND ((sender_id = ?) OR (recipient_id = ?));";

        List<Transfer> filteredTransfers = new ArrayList<>();
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, status_id, userId, userId);

        while(rowSet.next()){
            Transfer transfer = mapTransferFromRowSet(rowSet);
            filteredTransfers.add(transfer);
        }

        return filteredTransfers;
    }

    @Override
    public void updateTransferStatus(int transferId, int statusId) {
        Transfer transferToUpdate = getTransferById(transferId);

            if (statusId == 3) {
                String sql = "UPDATE transfer SET status_id = ? WHERE transfer_id = ?;";
                jdbcTemplate.update(sql, statusId, transferToUpdate.getTransferId());
            } else if (statusId == 1) {
                //&& accountDao.getAccountWithUserID(transferToUpdate.getSenderID()).getBalance() >= transferToUpdate.getAmount()){
                String sql = "UPDATE transfer SET status_id = ? WHERE transfer_id = ?;";
                jdbcTemplate.update(sql, statusId, transferToUpdate.getTransferId());
                accountDao.addToBalanceWithID(accountDao.getAccountWithUserID(transferToUpdate.getRecipientID()).getAccountId(), transferToUpdate.getAmount());
                accountDao.subtractFromBalanceWithID(accountDao.getAccountWithUserID(transferToUpdate.getSenderID()).getAccountId(), transferToUpdate.getAmount());
            }

    }

    @Override
    public void sendMoney(int userId, String username, double amountToSend) {
        Account principalAccount = accountDao.getAccountWithUserID(userId);
        Account receivingAccount = accountDao.getAccountWithUserID(userDao.findIdByUsername(username));

        String sql = "INSERT INTO transfer " +
                "(sender_id, recipient_id, status_id, type_id, amount) " +
                "VALUES (?,?,1,1,?);";


        jdbcTemplate.update(sql, principalAccount.getUserID(), receivingAccount.getUserID(), amountToSend);
        accountDao.addToBalanceWithID(receivingAccount.getAccountId(), amountToSend);
        accountDao.subtractFromBalanceWithID(principalAccount.getAccountId(), amountToSend);
    }

    @Override
    public void requestMoney(String username, int userId, double amountWanted) {
        Account principalAccount = accountDao.getAccountWithUserID(userId);
        Account requestedAccount = accountDao.getAccountWithUserID(userDao.findIdByUsername(username));

        String sql = "INSERT INTO transfer " +
                "(sender_id, recipient_id, status_id, type_id, amount) " +
                "VALUES (?,?,2,2,?);";
        jdbcTemplate.update(sql, requestedAccount.getUserID(), principalAccount.getUserID(), amountWanted);
    }


    private Transfer mapTransferFromRowSet(SqlRowSet rowSet){

        Transfer transfer = new Transfer();
        transfer.setTransferId(rowSet.getInt("transfer_id"));
        transfer.setSenderID(rowSet.getInt("sender_id"));
        transfer.setRecipientID(rowSet.getInt("recipient_id"));
        transfer.setStatusId(rowSet.getInt("status_id"));
        transfer.setTypeId(rowSet.getInt("type_id"));
        transfer.setAmount(rowSet.getDouble("amount"));

        return transfer;

    }


}
