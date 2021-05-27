package com.mike.bankapi.controller;

import com.mike.bankapi.model.dao.*;
import com.mike.bankapi.model.entity.Account;
import com.mike.bankapi.model.entity.Card;
import com.mike.bankapi.model.entity.Client;
import com.mike.bankapi.service.Utils;

import java.math.BigDecimal;
import java.util.List;

public class ClientController {
    private final DAOFactory daoFactory;
    private final ClientDAO clientDAO;
    private final AccountDAO accountDAO;
    private final CardDAO cardDAO;

    public ClientController(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
        clientDAO = daoFactory.getClientDAO();
        accountDAO = daoFactory.getAccountDAO();
        cardDAO = daoFactory.getCardDAO();
    }

    public List<Account> getAllClientAccounts(long clientId) throws DAOException {
        return accountDAO.getAllAccountsByClientId(clientId);
    }

    public List<Card> getAllClientCards(long clientId) throws DAOException {
        clientDAO.getClientById(clientId); //проверка что клиент существует с выбросом exception
        return clientDAO.getAllClientCards(clientId);
    }

    public Account getAccountById(long accountId) throws DAOException {
        return accountDAO.getAccountById(accountId);
    }

    public Card getCardById(long cardId) throws DAOException {
        return cardDAO.getCardById(cardId);
    }

    public boolean addFundsToAccount(long accountId, BigDecimal funds) throws DAOException {
        return accountDAO.addFundsToAccount(accountId, funds);
    }

    public List<Card> getAllAccountCards(long accountId) throws DAOException {
        accountDAO.getAccountById(accountId); //проверка что счет существует с выбросом exception
        return cardDAO.getAllCardsByAccountId(accountId);
    }

    public long createNewAccount(long clientId) throws DAOException {
        clientDAO.getClientById(clientId); //проверка что клиент существует с выбросом exception

        String number;

        do {
            number = Utils.generateNewNumber(20);
        } while (accountDAO.isAccountExists(number));

        Account account = new Account();
        account.setClientId(clientId);
        account.setNumber(number);
        account.setBalance(new BigDecimal("0.00"));
        return accountDAO.addNewAccount(account);
    }

    public long createNewCard(long clientId, long accountId, BigDecimal limit) throws DAOException {
        //если получили -1 в account_id, то создаем новый счет
        if (accountId == -1) {
            accountId = createNewAccount(clientId);
        }

        Account account = accountDAO.getAccountById(accountId); //проверка что клиент существует с выбросом exception
        if (account.getClientId() != clientId) {
            String error = "Переданный id счета не соответствует переданному id клиента";
            Utils.printMessage(error);
            throw new DAOException(error);
        }

        String number;
        do {
            number = Utils.generateNewNumber(16);
        } while (cardDAO.isCardExists(number));

        Card card = new Card();
        card.setAccountId(accountId);
        card.setCardNumber(number);
        card.setDailyLimit(limit);
        return cardDAO.addNewCard(card);
    }

    public List<Client> getAllData() throws DAOException {
        List<Client> clientList = clientDAO.getAllClients();
        for (Client client : clientList) {
            client.setAccountList(getAllClientAccounts(client.getClientId()));
            for (Account account : client.getAccountList()) {
                account.setCardList(getAllAccountCards(account.getAccountId()));
            }
        }
        return clientList;
    }
}
