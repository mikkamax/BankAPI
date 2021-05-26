package com.mike.bankapi.model.dao;

import com.mike.bankapi.model.entity.Account;
import com.mike.bankapi.model.entity.Client;

import java.math.BigDecimal;
import java.util.List;

public interface AccountDAO {
    public long addNewAccount(Account account) throws DAOException;
    public List<Account> getAllAccountsByClientId(long clientId) throws DAOException;
    public Account getAccountById(long accountId) throws DAOException;
    public boolean addFundsToAccount(long accountId, BigDecimal funds) throws DAOException;
}
