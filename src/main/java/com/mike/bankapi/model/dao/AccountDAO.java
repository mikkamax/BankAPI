package com.mike.bankapi.model.dao;

import com.mike.bankapi.model.entity.Account;

import java.math.BigDecimal;
import java.util.List;

public interface AccountDAO {
    long addNewAccount(Account account) throws DAOException;
    List<Account> getAllAccountsByClientId(long clientId) throws DAOException;
    Account getAccountById(long accountId) throws DAOException;
    boolean isAccountExists(String number) throws DAOException;
    boolean addFundsToAccount(long accountId, BigDecimal funds) throws DAOException;
}
