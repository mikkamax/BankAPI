package com.mike.bankapi.model.dao.h2;

import com.mike.bankapi.model.dao.AccountDAO;
import com.mike.bankapi.model.dao.DAOException;
import com.mike.bankapi.model.dao.DAOFactory;
import com.mike.bankapi.model.entity.Account;
import com.mike.bankapi.service.Utils;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class AccountDAOH2Test {
    public static AccountDAO accountDAO;

    @BeforeClass
    public static void initDb() throws DAOException {
        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.H2_DB);
        daoFactory.initDb();
        accountDAO = daoFactory.getAccountDAO();
    }

    @Test
    public void addNewAccount() throws DAOException {
        String number = Utils.generateNewNumber(20);

        Account expected = new Account();
        expected.setClientId(1);
        expected.setNumber(number);
        expected.setBalance(new BigDecimal("0.00"));

        long accountId = accountDAO.addNewAccount(expected);
        Account actual = accountDAO.getAccountById(accountId);

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void getAllAccountsByClientId() throws DAOException {
        Account account1 = new Account();
        account1.setClientId(2);
        account1.setNumber("40545104200000001272");
        account1.setBalance(new BigDecimal("12000.00"));

        Account account2 = new Account();
        account2.setClientId(2);
        account2.setNumber("40261494800000008428");
        account2.setBalance(new BigDecimal("39076.09"));

        List<Account> expectedList = Arrays.asList(account1, account2);
        List<Account> actualList = accountDAO.getAllAccountsByClientId(2);

        Assert.assertEquals(expectedList, actualList);
    }

    @Test
    public void getAccountById() throws DAOException {
        Account expected = new Account();
        expected.setClientId(6);
        expected.setNumber("40388647900000002618");
        expected.setBalance(new BigDecimal("95432.45"));

        Account actual = accountDAO.getAccountById(7);

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void addFundsToAccount() throws DAOException {
        Account account = accountDAO.getAccountById(1);
        BigDecimal balanceToAdd = new BigDecimal("999.99");
        accountDAO.addFundsToAccount(1, balanceToAdd);

        Account accountUpdated = accountDAO.getAccountById(1);

        Assert.assertEquals(account.getBalance().add(balanceToAdd), accountUpdated.getBalance());
    }

    @Test
    public void isAccountExists() throws DAOException {
        Account account = accountDAO.getAccountById(1);
        Assert.assertEquals(true, accountDAO.isAccountExists(account.getNumber()));
        Assert.assertEquals(false, accountDAO.isAccountExists("1111"));
    }
}