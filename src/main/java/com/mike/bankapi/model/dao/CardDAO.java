package com.mike.bankapi.model.dao;

import com.mike.bankapi.model.entity.Card;

import java.util.List;

public interface CardDAO {
    public long addNewCard(Card card) throws DAOException;
    public List<Card> getAllCardsByAccountId(long accountId) throws DAOException;
    public Card getCardById(long cardId) throws DAOException;
}
