package com.mike.bankapi.model.dao.h2;

import com.mike.bankapi.model.dao.ClientDAO;
import com.mike.bankapi.model.dao.DAOException;
import com.mike.bankapi.model.dao.DAOFactory;
import com.mike.bankapi.model.entity.Card;
import com.mike.bankapi.model.entity.Client;
import org.junit.*;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class ClientDAOH2Test {
    public static ClientDAO clientDAO;

    @BeforeClass
    public static void initDb() throws DAOException {
        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.H2_DB);
        daoFactory.initDb();
        clientDAO = daoFactory.getClientDAO();
    }

    @Test
    public void getAllClients() throws DAOException {
        List<Client> clientList = clientDAO.getAllClients();

        Client client4 = new Client();
        client4.setLastName("Захарова");
        client4.setFirstName("Берта");
        client4.setMiddleName("Геннадиевна");
        client4.setDateOfBirth(Date.valueOf(LocalDate.of(1986, 05, 06)));
        client4.setPassportNum("4290421116");
        Assert.assertEquals(client4, clientList.get(4));

        Client client7 = new Client();
        client7.setLastName("Ситников");
        client7.setFirstName("Алексей");
        client7.setMiddleName("Агафонович");
        client7.setDateOfBirth(Date.valueOf(LocalDate.of(1951, 03, 10)));
        client7.setPassportNum("4087399807");

        Assert.assertEquals(client7, clientList.get(6));
    }

    @Test
    public void getAllClientCards() throws DAOException {
        Card card1 = new Card();
        card1.setAccountId(11);
        card1.setCardNumber("5810508661245030");
        card1.setDailyLimit(new BigDecimal("0.00"));

        Card card2 = new Card();
        card2.setAccountId(12);
        card2.setCardNumber("4342839231641273");
        card2.setDailyLimit(new BigDecimal("0.00"));

        Card card3 = new Card();
        card3.setAccountId(13);
        card3.setCardNumber("5800850144293158");
        card3.setDailyLimit(new BigDecimal("0.00"));

        Card card4 = new Card();
        card4.setAccountId(13);
        card4.setCardNumber("4973986170896476");
        card4.setDailyLimit(new BigDecimal("3000.00"));

        Card card5 = new Card();
        card5.setAccountId(13);
        card5.setCardNumber("5205147940914498");
        card5.setDailyLimit(new BigDecimal("500.00"));

        List<Card> expectedList = Arrays.asList(card1, card2, card3, card4, card5);
        List<Card> actualList = clientDAO.getAllClientCards(10);

        Assert.assertEquals(expectedList, actualList);
    }

    @Test
    public void getClientById() throws DAOException {
        Client client = new Client();
        client.setLastName("Блинова");
        client.setFirstName("Валерия");
        client.setMiddleName("");
        client.setDateOfBirth(Date.valueOf(LocalDate.of(1956, 11, 10)));
        client.setPassportNum("4363356897");

        Assert.assertEquals(client, clientDAO.getClientById(8));
    }
}