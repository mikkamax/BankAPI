package com.mike.bankapi.model.dao.h2;

import com.mike.bankapi.model.dao.AccountDAO;
import com.mike.bankapi.model.dao.DAOException;
import com.mike.bankapi.model.dao.DAOFactory;
import com.mike.bankapi.model.entity.Account;
import com.mike.bankapi.service.Utils;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AccountDAOH2 implements AccountDAO {
    private final DAOFactory daoFactory;

    public AccountDAOH2(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    @Override
    public long addNewAccount(Account account) throws DAOException {
        String addNewAccountSql = "INSERT INTO account (client_id, number, balance) VALUES (?, ?, ?);";

        try (Connection connection = daoFactory.getConnection();
             PreparedStatement pStatement = connection.prepareStatement(addNewAccountSql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            pStatement.setLong(1, account.getClientId());
            pStatement.setString(2, account.getNumber());
            pStatement.setBigDecimal(3, account.getBalance());
            pStatement.executeUpdate();

            ResultSet resultSet = pStatement.getGeneratedKeys();
            resultSet.next();
            return resultSet.getLong("_id");
        } catch (SQLException e) {
            Utils.printMessage("Ошибка при добавлении нового счета");
            throw new DAOException("Ошибка при добавлении нового счета", e);
        }
    }

    @Override
    public List<Account> getAllAccountsByClientId(long clientId) throws DAOException {
        String getAllAccountsByClientIdSql = "SELECT * FROM account WHERE client_id = ?;";

        try (Connection connection = daoFactory.getConnection();
             PreparedStatement pStatement = connection.prepareStatement(getAllAccountsByClientIdSql)) {
            pStatement.setLong(1, clientId);
            pStatement.execute();
            ResultSet resultSet = pStatement.getResultSet();

            List<Account> list = new ArrayList<>();
            while (resultSet.next()) {
                list.add(getAccountFromResultSet(resultSet));
            }

            return list;
        } catch (SQLException e) {
            Utils.printMessage("Ошибка! Не удалось получить все счета клиента");
            throw new DAOException("Ошибка! Не удалось получить все счета клиента", e);
        }
    }

    @Override
    public Account getAccountById(long accountId) throws DAOException {
        String getAccountByIdSql = "SELECT * FROM account WHERE _id = ?;";

        try (Connection connection = daoFactory.getConnection();
             PreparedStatement pStatement = connection.prepareStatement(getAccountByIdSql)) {
            pStatement.setLong(1, accountId);
            pStatement.execute();

            ResultSet resultSet = pStatement.getResultSet();
            resultSet.next();

            return getAccountFromResultSet(resultSet);
        } catch (SQLException e) {
            Utils.printMessage("Ошибка! Не удалось получить счет по Id");
            throw new DAOException("Ошибка! Не удалось получить счет по Id", e);
        }
    }

    @Override
    public boolean addFundsToAccount(long accountId, BigDecimal funds) throws DAOException {
        Account account = getAccountById(accountId);

        String addFundsSql = "UPDATE account SET balance = ? WHERE _id = ?";
        try (Connection connection = daoFactory.getConnection();
             PreparedStatement pStatement = connection.prepareStatement(addFundsSql)) {
            BigDecimal newBalance = account.getBalance().add(funds);
            pStatement.setBigDecimal(1, newBalance);
            pStatement.setLong(2, accountId);
            boolean result = pStatement.executeUpdate() == 1 ? true : false;
            return result;
        } catch (SQLException e) {
            Utils.printMessage("Ошибка при добавлении средств на счет");
            throw new DAOException("Ошибка при добавлении средств на счет", e);
        }
    }

    private Account getAccountFromResultSet(ResultSet rs) throws SQLException {
        Account account = new Account();
        account.setAccountId(rs.getLong("_id"));
        account.setClientId(rs.getLong("client_id"));
        account.setNumber(rs.getString("number"));
        account.setBalance(rs.getBigDecimal("balance"));
        return account;
    }
}
