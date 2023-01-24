package com.techelevator.dao;

import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.dao.JdbcTransferDao;
import com.techelevator.tenmo.dao.JdbcUserDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import java.util.List;


public class JdbcTransferDaoTest extends BaseDaoTests{

    private JdbcUserDao userDao;
    private JdbcAccountDao accountDao;
    private JdbcTransferDao transferDao;

    @Before
    public void setup() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        userDao = new JdbcUserDao(jdbcTemplate);
        accountDao = new JdbcAccountDao(jdbcTemplate);
        transferDao = new JdbcTransferDao(jdbcTemplate);
    }

    //Integration testing is required on each method that connects to the database.

    @Test
    public void getListOfTransfersHappy() {
        Account testAccount = accountDao.getAccountWithUserID(1001);
        List<Transfer> testList = transferDao.getListOfTransfers(testAccount.getUserID());
        int expectedLength = 2;
        Assert.assertEquals(expectedLength, testList.size());
    }

    @Test
    public void getListOfTransfersBroken() {
        //testAccount is null because the account doesn't exist
        Account testAccount = accountDao.getAccountWithUserID(1);
        List<Transfer> testList = transferDao.getListOfTransfers(testAccount.getUserID());
        int expectedLength = 0;
        Assert.assertEquals(expectedLength, testList.size());
    }

    @Test
    public void getTransferById() {
        Account testAccount = accountDao.getAccountWithUserID(1001);
        Transfer transfer = transferDao.getTransferById(3001);
        Transfer expectedTransfer = new Transfer();
        expectedTransfer.setSenderID(1001);
        expectedTransfer.setRecipientID(1002);
        expectedTransfer.setStatusId(1);
        expectedTransfer.setTypeId(1);
        expectedTransfer.setAmount(300);

        Assert.assertEquals(expectedTransfer.getSenderID(), transfer.getSenderID());
        Assert.assertEquals(expectedTransfer.getRecipientID(), transfer.getRecipientID());
        Assert.assertEquals(expectedTransfer.getStatusId(), transfer.getStatusId());
        Assert.assertEquals(expectedTransfer.getTypeId(), transfer.getTypeId());
        Assert.assertEquals(expectedTransfer.getAmount(), transfer.getAmount(), .00001);

    }

    @Test
    public void filterTransfersByStatusHappy() {
        Account testAccount = accountDao.getAccountWithUserID(1001);
        List<Transfer> filteredTransfer = transferDao.filterTransfersByStatus(1, testAccount.getUserID());
        int expectedLength = 1;
        Assert.assertEquals(expectedLength, filteredTransfer.size());

        filteredTransfer = transferDao.filterTransfersByStatus(2, testAccount.getUserID());
        expectedLength = 1;
        Assert.assertEquals(expectedLength, filteredTransfer.size());

        filteredTransfer = transferDao.filterTransfersByStatus(3, testAccount.getUserID());
        expectedLength = 0;
        Assert.assertEquals(expectedLength, filteredTransfer.size());
    }

    @Test
    public void updateTransferStatusSuccess() {
        Transfer transferToUpdate = transferDao.getTransferById(3001);
        transferDao.updateTransferStatus(3001, 1);

        double expectedBalance = 700;
        double actualBalance = accountDao.getBalanceWithUserID(1001);
        Assert.assertEquals(expectedBalance, actualBalance, .0001);

        expectedBalance = 1300;
        actualBalance = accountDao.getBalanceWithUserID(1002);
        Assert.assertEquals(expectedBalance, actualBalance, .0001);

        int actualId = transferToUpdate.getStatusId();
        int expectedId = 1;
        Assert.assertEquals(expectedId, actualId);
    }

    @Test
    public void updateTransferStatusRejection() {
        Transfer transferToUpdate = transferDao.getTransferById(3001);
        transferDao.updateTransferStatus(3, 3001);

        double expectedBalance = 1000;
        double actualBalance = accountDao.getBalanceWithUserID(1001);
        Assert.assertEquals(expectedBalance, actualBalance, .0001);

        expectedBalance = 1000;
        actualBalance = accountDao.getBalanceWithUserID(1002);
        Assert.assertEquals(expectedBalance, actualBalance, .0001);

        int actualId = transferToUpdate.getStatusId();
        int expectedId = 3;
        Assert.assertEquals(expectedId, actualId);
    }

    @Test
    public void sendMoneySuccess() {
        Account testAccount = accountDao.getAccountWithUserID(1001);
        Account testAccount2 = accountDao.getAccountWithUserID(1002);
        double expectedBalance = 500;
        transferDao.sendMoney(1001, "user", 500);
        Assert.assertEquals(expectedBalance, testAccount.getBalance(), .001);
        expectedBalance = 1500;
        Assert.assertEquals(expectedBalance, testAccount2.getBalance(), .0001);
    }
    @Test
    public void sendMoneyFailure() {
        Account testAccount = accountDao.getAccountWithUserID(1001);
        Account testAccount2 = accountDao.getAccountWithUserID(1002);
        double expectedBalance = 1000;
        transferDao.sendMoney(1001, "user", 1500);
        Assert.assertEquals(expectedBalance, testAccount.getBalance(), .001);
        Assert.assertEquals(expectedBalance, testAccount2.getBalance(), .0001);
    }
    @Test
    public void requestMoney() {
        Account testAccount = accountDao.getAccountWithUserID(1001);
        Account testAccount2 = accountDao.getAccountWithUserID(1002);
        transferDao.requestMoney("user", 1001,  500);
        List<Transfer> filterForPendingRequests = transferDao.filterTransfersByStatus(2, 1002);
        Assert.assertEquals(1, filterForPendingRequests.size());
    }
}
