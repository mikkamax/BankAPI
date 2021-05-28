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
            String error = "Ошибка при добавлении нового счета";
            Utils.printMessage(error);
            throw new DAOException(error, e);
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
            String error = "Ошибка! Не удалось получить все счета клиента";
            Utils.printMessage(error);
            throw new DAOException(error, e);
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
            String error = "Ошибка! Не удалось получить счет по Id";
            Utils.printMessage(error);
            throw new DAOException(error, e);
        }
    }

    @Override
    public boolean isAccountExists(String number) throws DAOException {
        String isAccountExistsSql = "SELECT COUNT(*) FROM account WHERE number = ?;";

        try (Connection connection = daoFactory.getConnection();
             PreparedStatement pStatement = connection.prepareStatement(isAccountExistsSql)) {
            pStatement.setString(1, number);
            pStatement.execute();

            ResultSet resultSet = pStatement.getResultSet();
            resultSet.next();

            return resultSet.getInt(1) > 0;
        } catch (SQLException e) {
            String error = "Ошибка! Запрос на существование счета в БД не выполнен";
            Utils.printMessage(error);
            throw new DAOException(error, e);
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
            String error = "Ошибка при добавлении средств на счет";
            Utils.printMessage(error);
            throw new DAOException(error, e);
        }
    }

    /**
     * Парсинг ResultSet для получения Account
     * @param rs с наведенным на строку указателем
     * @return объект Account
     * @throws SQLException
     */
    private Account getAccountFromResultSet(ResultSet rs) throws SQLException {
        Account account = new Account();
        account.setAccountId(rs.getLong("_id"));
        account.setClientId(rs.getLong("client_id"));
        account.setNumber(rs.getString("number"));
        account.setBalance(rs.getBigDecimal("balance"));
        return account;
    }
}
