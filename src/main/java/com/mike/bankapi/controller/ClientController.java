package com.mike.bankapi.controller;

import com.mike.bankapi.model.dao.*;
import com.mike.bankapi.model.entity.Account;
import com.mike.bankapi.model.entity.Card;
import com.mike.bankapi.model.entity.Client;
import com.mike.bankapi.service.Utils;

import java.math.BigDecimal;
import java.util.List;

/**
 * Контроллер служит связующим звеном между запросами HttpServer и слоем ДАО
 */

public class ClientController {
    private final DAOFactory daoFactory;
    private final ClientDAO clientDAO;
    private final AccountDAO accountDAO;
    private final CardDAO cardDAO;

    /**
     * @param daoFactory инстанс конкретной релазации DAOFactory
     */
    public ClientController(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
        clientDAO = daoFactory.getClientDAO();
        accountDAO = daoFactory.getAccountDAO();
        cardDAO = daoFactory.getCardDAO();
    }

    /**
     * Получение списка всех счетов клиента из ДАО по id клиента
     * @param clientId id клиента в базе данных
     * @return List<Account> список счетов
     * @throws DAOException если клиента с таким id не существует или при ошибке БД
     */
    public List<Account> getAllClientAccounts(long clientId) throws DAOException {
        return accountDAO.getAllAccountsByClientId(clientId);
    }

    /**
     * Получение списка всех карт клиента из ДАО по id клиента
     * @param clientId id клиента в базе данных
     * @return List<Сard> список карт
     * @throws DAOException если клиента с таким id не существует или при ошибке БД
     */
    public List<Card> getAllClientCards(long clientId) throws DAOException {
        clientDAO.getClientById(clientId); //проверка что клиент существует с выбросом exception
        return clientDAO.getAllClientCards(clientId);
    }

    /**
     * Получение счета из ДАО по id карты
     * @param accountId id счета в базе данных
     * @return Account объекта счета
     * @throws DAOException если счета с таким id не существует или при ошибке БД
     */
    public Account getAccountById(long accountId) throws DAOException {
        return accountDAO.getAccountById(accountId);
    }

    /**
     * Получение карты из ДАО по id карты
     * @param cardId id карты в базе данных
     * @return Card объект карты
     * @throws DAOException если карта с таким id не существует или при ошибке БД
     */
    public Card getCardById(long cardId) throws DAOException {
        return cardDAO.getCardById(cardId);
    }

    /**
     * Добавление средств на счет в ДАО
     * @param accountId id счета в базе данных
     * @param funds сумма средств для пополнения
     * @return boolean true, если все прошло успешно или false если возникла ошибка
     * @throws DAOException если счета с таким id не существует или при ошибке БД
     */
    public boolean addFundsToAccount(long accountId, BigDecimal funds) throws DAOException {
        return accountDAO.addFundsToAccount(accountId, funds);
    }

    /**
     * Получение списка всех карт счета из ДАО по id счета
     * @param accountId id счета в базе данных
     * @return List<Сard> список карт
     * @throws DAOException если счета с таким id не существует или при ошибке БД
     */
    public List<Card> getAllAccountCards(long accountId) throws DAOException {
        accountDAO.getAccountById(accountId); //проверка что счет существует с выбросом exception
        return cardDAO.getAllCardsByAccountId(accountId);
    }

    /**
     * Создание нового счета клиента и передача его в ДАО
     * @param clientId id клиента в базе данных
     * @return long id открытого счета в базе данных или 0, если счет не был открыт
     * @throws DAOException если клиента с таким id не существует или при ошибке БД
     */
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

    /**
     * Создание новой карты (и, при необходимости счета) клиента и передача ее в ДАО
     * @param clientId id клиента в базе данных
     * @param accountId id счета в базе данных, либо -1 если клиенту нужно создать новый счет
     * @param limit лимит по карте, 0.00 если лимита нета
     * @return long id открытой карты в базе данных или 0, если карта не была открыта
     * @throws DAOException если клиента с таким id не существует, если id счета не соответствует id клиента или при ошибке БД
     */
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

    /**
     * Получение всех данных по клиенту (клиент, счета, карты) из ДАО по id клиента
     * @param clientId id клиента в базе данных
     * @return Cleint - объект клиента
     * @throws DAOException если клиента с таким id не существует или при ошибке БД
     */
    public Client getAllClientDataById(long clientId) throws DAOException {
        Client client = clientDAO.getClientById(clientId);
        client.setAccountList(accountDAO.getAllAccountsByClientId(client.getClientId()));
        for (Account account : client.getAccountList()) {
            account.setCardList(getAllAccountCards(account.getAccountId()));
        }
        return client;
    }

    /**
     * !!!ВНИМАНИЕ!!! ТЕСТОВЫЙ МЕТОД! После прохождения тестов - сжечь
     * Тестовый метод для получения всех данных из БД через ДАО
     * @return List<Client> список всех клиентов со всеми счетами и всеми картами
     * @throws DAOException при ошибке в БД
     */
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
