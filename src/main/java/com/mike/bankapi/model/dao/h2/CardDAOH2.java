package com.mike.bankapi.model.dao.h2;

import com.mike.bankapi.model.dao.CardDAO;
import com.mike.bankapi.model.dao.DAOException;
import com.mike.bankapi.model.dao.DAOFactory;
import com.mike.bankapi.model.entity.Card;
import com.mike.bankapi.service.Utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CardDAOH2 implements CardDAO {
    private final DAOFactory daoFactory;

    public CardDAOH2(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    @Override
    public long addNewCard(Card card) throws DAOException {
        String addNewCardSql = "INSERT INTO card (account_id, card_number, daily_limit) VALUES (?, ?, ?);";

        try (Connection connection = daoFactory.getConnection();
             PreparedStatement pStatement = connection.prepareStatement(addNewCardSql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            pStatement.setLong(1, card.getAccountId());
            pStatement.setString(2, card.getCardNumber());
            pStatement.setBigDecimal(3, card.getDailyLimit());
            pStatement.executeUpdate();

            ResultSet resultSet = pStatement.getGeneratedKeys();
            resultSet.next();
            return resultSet.getLong("_id");
        } catch (SQLException e) {
            Utils.printMessage("Ошибка при добавлении новой карты");
            throw new DAOException("Ошибка при добавлении новой карты", e);
        }
    }

    @Override
    public List<Card> getAllCardsByAccountId(long accountId) throws DAOException {
        String getAllCardsByAccountIdSql = "SELECT * FROM card WHERE account_id = ?;";

        try (Connection connection = daoFactory.getConnection();
             PreparedStatement pStatement = connection.prepareStatement(getAllCardsByAccountIdSql)) {
            pStatement.setLong(1, accountId);
            pStatement.execute();
            ResultSet resultSet = pStatement.getResultSet();

            List<Card> list = new ArrayList<>();
            while (resultSet.next()) {
                list.add(getCardFromResultSet(resultSet));
            }

            return list;
        } catch (SQLException e) {
            Utils.printMessage("Ошибка! Не удалось получить все карты по счету");
            throw new DAOException("Ошибка! Не удалось получить все карты по счету", e);
        }
    }

    @Override
    public Card getCardById(long cardId) throws DAOException {
        String getCardByIdSql = "SELECT * FROM card WHERE _id = ?;";

        try (Connection connection = daoFactory.getConnection();
             PreparedStatement pStatement = connection.prepareStatement(getCardByIdSql)) {
            pStatement.setLong(1, cardId);
            pStatement.execute();

            ResultSet resultSet = pStatement.getResultSet();
            resultSet.next();

            return getCardFromResultSet(resultSet);
        } catch (SQLException e) {
            Utils.printMessage("Ошибка! Не удалось получить карту по Id");
            throw new DAOException("Ошибка! Не удалось получить карту по Id", e);
        }
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
