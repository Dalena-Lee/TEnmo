package com.techelevator.dao;

import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.dao.JdbcUserDao;
import com.techelevator.tenmo.model.Account;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;


public class JdbcAccountDaoTest extends BaseDaoTests {

    private JdbcUserDao sut;
    private JdbcAccountDao dao;

    @Before
    public void setup() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        dao = new JdbcAccountDao(jdbcTemplate);
        sut = new JdbcUserDao(jdbcTemplate);
    }

    @Test
    public void getBalanceWithUserID() {
        int userID = sut.findIdByUsername("bob");
        double expectedBalance = 1000.00;
        double actualBalance = dao.getBalanceWithUserID(userID);
        Assert.assertEquals(expectedBalance, actualBalance, 0);

        userID = sut.findIdByUsername("user");
        actualBalance = dao.getBalanceWithUserID(userID);
        Assert.assertEquals(expectedBalance, actualBalance, 0);
    }

    @Test
    public void addToBalanceWithID() {
        double expectedBalance = 1050.00;
        dao.addToBalanceWithID(2001, 50.0);
        double actualBalance = dao.getBalanceWithUserID(1001);
        Assert.assertEquals(expectedBalance, actualBalance, 0);
    }

    @Test
    public void subtractFromBalanceWithID() {
        double expectedBalance = 950.00;
        dao.subtractFromBalanceWithID(2001, 50.0);
        double actualBalance = dao.getBalanceWithUserID(1001);
        Assert.assertEquals(expectedBalance, actualBalance, 0);
    }

    @Test
    public void getAccountWithUserID() {
        int userID = sut.findIdByUsername("bob");
        Account testAccount = dao.getAccountWithUserID(userID);

        Assert.assertEquals(2001, testAccount.getAccountId());
        Assert.assertEquals(1001, testAccount.getUserID());
        Assert.assertEquals(1000.00, testAccount.getBalance(), 0);

        userID = sut.findIdByUsername("user");
        testAccount = dao.getAccountWithUserID(userID);
        Assert.assertEquals(2002, testAccount.getAccountId());
        Assert.assertEquals(1002, testAccount.getUserID());
        Assert.assertEquals(1000.00, testAccount.getBalance(), 0);
    }

}