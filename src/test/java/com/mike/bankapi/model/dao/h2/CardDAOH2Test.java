package com.mike.bankapi.model.dao.h2;

import com.mike.bankapi.model.dao.CardDAO;
import com.mike.bankapi.model.dao.DAOException;
import com.mike.bankapi.model.dao.DAOFactory;
import com.mike.bankapi.model.entity.Card;
import com.mike.bankapi.service.Utils;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

public class CardDAOH2Test {
    public static CardDAO cardDAO;

    @BeforeClass
    public static void initDb() throws DAOException {
        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.H2_DB);
        daoFactory.initDb();
        cardDAO = daoFactory.getCardDAO();
    }

    @Test
    public void addNewCard() throws DAOException {
        String number = Utils.generateNewNumber(16);

        Card card = new Card();
        card.setAccountId(1);
        card.setCardNumber(number);
        card.setDailyLimit(new BigDecimal("0.00"));

        long cardId = cardDAO.addNewCard(card);

        Assert.assertEquals(card, cardDAO.getCardById(cardId));
    }

    @Test
    public void getAllCardsByAccountId() throws DAOException {
        Card card1 = new Card();
        card1.setAccountId(13);
        card1.setCardNumber("5800850144293158");
        card1.setDailyLimit(new BigDecimal("0.00"));

        Card card2 = new Card();
        card2.setAccountId(13);
        card2.setCardNumber("4973986170896476");
        card2.setDailyLimit(new BigDecimal("3000.00"));

        Card card3 = new Card();
        card3.setAccountId(13);
        card3.setCardNumber("5205147940914498");
        card3.setDailyLimit(new BigDecimal("500.00"));

        List<Card> expectedList = Arrays.asList(card1, card2, card3);
        List<Card> actualList = cardDAO.getAllCardsByAccountId(13);

        Assert.assertEquals(expectedList, actualList);
    }

    @Test
    public void getCardById() throws DAOException {
        Card card = new Card();
        card.setAccountId(8);
        card.setCardNumber("5470487992351756");
        card.setDailyLimit(new BigDecimal("0.00"));

        Assert.assertEquals(card, cardDAO.getCardById(6));
    }

    @Test
    public void isCardExists() throws DAOException {
        Card card = cardDAO.getCardById(1);
        Assert.assertEquals(true, cardDAO.isCardExists(card.getCardNumber()));
        Assert.assertEquals(false, cardDAO.isCardExists("999"));
    }
}