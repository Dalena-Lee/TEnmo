package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.security.Principal;

@Component
public class JdbcAccountDao implements AccountDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    private UserDao userDao;

    public JdbcAccountDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Account getAccountWithUserID(int user_id) {
        String sql = "SELECT * FROM account WHERE user_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, user_id);
        Account account = null;

        if (results.next()) {
            account = mapRowToAccount(results);
            return account;
        }

        return account;
    }

    @Override
    public void addToBalanceWithID(int account_id, double amount){
        String sql = "UPDATE account SET balance = balance + ? WHERE account_id = ?;";
        this.jdbcTemplate.update(sql, amount, account_id);
    }

    @Override
    public void subtractFromBalanceWithID(int account_id, double amount) {
        String sql = "UPDATE account SET balance = balance - ? WHERE account_id = ?;";
        this.jdbcTemplate.update(sql, amount, account_id);
    }

    @Override
    public double getBalanceWithUserID(int user_id){
        String sql = "SELECT * FROM account WHERE user_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, user_id);
        if (results.next()){
            Account account = mapRowToAccount(results);
            return account.getBalance();
        }
        return 0;
    }

    private Account mapRowToAccount(SqlRowSet rs) {
        Account account = new Account();
        account.setAccountId(rs.getInt("account_id"));
        account.setUserID(rs.getInt("user_id"));
        BigDecimal bigDec = rs.getBigDecimal("balance");
        account.setBalance(bigDec.doubleValue());

//I HATE BIG DECIMAL STOP MAKING US USE IT WE WERE STUCK ON THIS FOREVER YOU HAVE NO IDEA BROTHER.
        return account;
    }

}
