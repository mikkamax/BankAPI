package com.mike.bankapi.model.dao.h2;

import com.mike.bankapi.model.dao.ClientDAO;
import com.mike.bankapi.model.dao.DAOException;
import com.mike.bankapi.model.dao.DAOFactory;
import com.mike.bankapi.model.entity.Card;
import com.mike.bankapi.model.entity.Client;
import com.mike.bankapi.service.Utils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClientDAOH2 implements ClientDAO {
    private final DAOFactory daoFactory;

    public ClientDAOH2(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    @Override
    public List<Client> getAllClients() throws DAOException {
        String getAllClientsSql = "SELECT * FROM client;";

        try (Connection connection = daoFactory.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute(getAllClientsSql);
            ResultSet resultSet = statement.getResultSet();

            List<Client> list = new ArrayList<>();
            while (resultSet.next()) {
                list.add(getClientFromResultSet(resultSet));
            }

            return list;
        } catch (SQLException e) {
            Utils.printMessage("Ошибка! Не удалось получить список всех клиентов");
            throw new DAOException("Ошибка! Не удалось получить список всех клиентов", e);
        }
    }

    @Override
    public List<Card> getAllClientCards(long clientId) throws DAOException {
        String getAllClientCards = "SELECT card._id, card.account_id, card.card_number, card.daily_limit " +
                "FROM card " +
                "INNER JOIN account ON account._id=card.account_id " +
                "WHERE account.client_id = ?";

        try (Connection connection = daoFactory.getConnection();
             PreparedStatement pStatement = connection.prepareStatement(getAllClientCards)) {
            pStatement.setLong(1, clientId);
            pStatement.execute();

            ResultSet resultSet = pStatement.getResultSet();
            List<Card> cardList = new ArrayList<>();
            while (resultSet.next()) {
                cardList.add(getCardFromResultSet(resultSet));
            }

            return cardList;
        } catch (SQLException e) {
            Utils.printMessage("Ошибка! Не удалось получить карты по Id клиента");
            throw new DAOException("Ошибка! Не удалось получить карты по Id клиента", e);
        }
    }

    @Override
    public Client getClientById(long clientId) throws DAOException {
        String getClientByIdSql = "SELECT * FROM client WHERE _id = ?;";

        try (Connection connection = daoFactory.getConnection();
             PreparedStatement pStatement = connection.prepareStatement(getClientByIdSql)) {
            pStatement.setLong(1, clientId);
            pStatement.execute();

            ResultSet resultSet = pStatement.getResultSet();
            resultSet.next();

            return getClientFromResultSet(resultSet);
        } catch (SQLException e) {
            Utils.printMessage("Ошибка! Не удалось получить клиента по Id");
            throw new DAOException("Ошибка! Не удалось получить клиента по Id", e);
        }
    }

    private Client getClientFromResultSet(ResultSet rs) throws SQLException {
        Client client = new Client();
        client.setClientId(rs.getInt("_id"));
        client.setLastName(rs.getString("last_name"));
        client.setFirstName(rs.getString("first_name"));
        client.setMiddleName(rs.getString("middle_name"));
        client.setDateOfBirth(rs.getDate("date_of_birth"));
        client.setPassportNum(rs.getString("passport_num"));
        return client;
    }

    private Card getCardFromResultSet(ResultSet rs) throws SQLException {
        Card card = new Card();
        card.setCardId(rs.getLong("_id"));
        card.setAccountId(rs.getLong("account_id"));
        card.setCardNumber(rs.getString("card_number"));
        card.setDailyLimit(rs.getBigDecimal("daily_limit"));
        return card;
    }
}
