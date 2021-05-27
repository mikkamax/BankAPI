package com.mike.bankapi.model.dao;

import com.mike.bankapi.model.entity.Card;

import java.util.List;

public interface CardDAO {
    long addNewCard(Card card) throws DAOException;
    List<Card> getAllCardsByAccountId(long accountId) throws DAOException;
    Card getCardById(long cardId) throws DAOException;
    boolean isCardExists(String number) throws DAOException;
}
