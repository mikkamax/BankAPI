package com.mike.bankapi.service;

import com.mike.bankapi.model.dao.*;
import com.mike.bankapi.model.entity.Account;
import com.mike.bankapi.model.entity.Card;
import com.mike.bankapi.model.entity.Client;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class ClientServiceTest {
    private static ClientService clientService;

    @BeforeClass
    public static void initDb() throws DAOException {
        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.H2_DB);
        daoFactory.initDb();
        clientService = new ClientService(daoFactory);
    }

    @Test
    public void getAllClientAccounts() throws DAOException {
        Account account1 = new Account();
        account1.setClientId(2);
        account1.setNumber("40545104200000001272");
        account1.setBalance(new BigDecimal("12000.00"));

        Account account2 = new Account();
        account2.setClientId(2);
        account2.setNumber("40261494800000008428");
        account2.setBalance(new BigDecimal("39076.09"));

        List<Account> expectedList = Arrays.asList(account1, account2);
        List<Account> actualList = clientService.getAllClientAccounts(2);

        Assert.assertEquals(expectedList, actualList);
    }

    @Test(expected = DAOException.class)
    public void getAllClientAccountsFailOnClientId() throws DAOException {
        clientService.getAllClientCards(20);
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
        List<Card> actualList = clientService.getAllClientCards(10);

        Assert.assertEquals(expectedList, actualList);
    }

    @Test(expected = DAOException.class)
    public void getAllClientCardsFailOnClientId() throws DAOException {
        clientService.getAllClientCards(20);
    }

    @Test
    public void getAccountById() throws DAOException {
        Account expected = new Account();
        expected.setClientId(6);
        expected.setNumber("40388647900000002618");
        expected.setBalance(new BigDecimal("95432.45"));

        Account actual = clientService.getAccountById(7);

        Assert.assertEquals(expected, actual);
    }

    @Test(expected = DAOException.class)
    public void getAccountByIdFailOnAccountId() throws DAOException {
        clientService.getAccountById(20);
    }

    @Test
    public void getCardById() throws DAOException {
        clientService.getCardById(5);
    }

    @Test(expected = DAOException.class)
    public void getCardByIdFailOnCardId() throws DAOException {
        clientService.getCardById(20);
    }

    @Test
    public void addFundsToAccount() throws DAOException {
        Account account = clientService.getAccountById(1);
        BigDecimal balanceToAdd = new BigDecimal("999.99");
        clientService.addFundsToAccount(1, balanceToAdd);

        Account accountUpdated = clientService.getAccountById(1);

        Assert.assertEquals(account.getBalance().add(balanceToAdd), accountUpdated.getBalance());
    }

    @Test(expected = DAOException.class)
    public void addFundsToAccountFailOnAccountId() throws DAOException {
        clientService.addFundsToAccount(20, new BigDecimal("15.00"));
    }

    @Test
    public void getAllAccountCards() throws DAOException {
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
        List<Card> actualList = clientService.getAllAccountCards(13);

        Assert.assertEquals(expectedList, actualList);
    }

    @Test(expected = DAOException.class)
    public void getAllAccountCardsFailOnAccountId() throws DAOException {
        clientService.getAllAccountCards(20);
    }

    @Test
    public void createNewAccount() throws DAOException {
        long newAccountId = clientService.createNewAccount(1);
        clientService.getAccountById(newAccountId); //???????? ??????????????, ???? ?????? ??????????????????
    }

    @Test(expected = DAOException.class)
    public void createNewAccountFailOnClientId() throws DAOException {
        long newAccountId = clientService.createNewAccount(20);
        clientService.getAccountById(newAccountId); //???????????? ???????????????? ??????????????
    }

    @Test
    public void createNewCard() throws DAOException {
        int oldSize = clientService.getAllAccountCards(1).size();
        long cardId = clientService.createNewCard(1, 1, new BigDecimal("0.00"));
        int newSize = clientService.getAllAccountCards(1).size();
        Assert.assertEquals(true, newSize > oldSize);

        clientService.getCardById(cardId);
    }

    @Test
    public void createNewCardAndNewAccount() throws DAOException {
        long oldAccountSize = clientService.getAllClientAccounts(1).size();
        long cardId = clientService.createNewCard(1, -1, new BigDecimal("0.00"));
        int newAccountSize = clientService.getAllClientAccounts(1).size();
        Assert.assertEquals(true, newAccountSize > oldAccountSize);

        clientService.getCardById(cardId);
    }

    @Test(expected = DAOException.class)
    public void createNewCardFailOnClientId() throws DAOException {
        clientService.createNewCard(0, -1, new BigDecimal("0.00"));
    }

    @Test(expected = DAOException.class)
    public void createNewCardFailOnClientIdNotMatchAccountId() throws DAOException {
        clientService.createNewCard(1, 2, new BigDecimal("0.00"));
    }

    @Test
    public void getAllClientDataById() throws DAOException {
        Client actualClient = clientService.getAllClientDataById(7);

        Client expectedClient = new Client();
        expectedClient.setLastName("????????????????");
        expectedClient.setFirstName("??????????????");
        expectedClient.setMiddleName("????????????????????");
        expectedClient.setDateOfBirth(Date.valueOf(LocalDate.of(1951, 03, 10)));
        expectedClient.setPassportNum("4087399807");

        Account account = new Account();
        account.setClientId(7);
        account.setNumber("50443335500000009240");
        account.setBalance(new BigDecimal("326780.40"));

        Card card = new Card();
        card.setAccountId(8);
        card.setCardNumber("5470487992351756");
        card.setDailyLimit(new BigDecimal("0.00"));

        assertEquals(expectedClient, actualClient);
        assertEquals(account, actualClient.getAccountList().get(0));
        assertEquals(card, actualClient.getAccountList().get(0).getCardList().get(0));
    }

    @Test
    public void getAllData() throws DAOException {
        List<Client> clientList = clientService.getAllData();

        Client client = new Client();
        client.setLastName("????????????????");
        client.setFirstName("??????????");
        client.setMiddleName("??????????????????????");
        client.setDateOfBirth(Date.valueOf(LocalDate.of(1986, 05, 06)));
        client.setPassportNum("4290421116");

        Account account = new Account();
        account.setClientId(5);
        account.setNumber("40187781400000001227");
        account.setBalance(new BigDecimal("230.87"));

        Card card = new Card();
        card.setAccountId(6);
        card.setCardNumber("5421814722123000");
        card.setDailyLimit(new BigDecimal("0.00"));

        Assert.assertEquals(client, clientList.get(4));
        Assert.assertEquals(account, clientList.get(4).getAccountList().get(0));
        Assert.assertEquals(card, clientList.get(4).getAccountList().get(0).getCardList().get(0));
    }
}