package com.mike.bankapi.model.dao;

import com.mike.bankapi.model.entity.Account;

import java.math.BigDecimal;
import java.util.List;

/**
 * Интерфейс для работы с клиентскими счетами, наследники должны реализовать все методы
 */

public interface AccountDAO {

    /**
     * Добавляет переданный счет в базу данных
     * @param account - объект счета для сохранения в БД
     * @return long - id созданного счета в БД
     * @throws DAOException если счет сохранить не удалось и при ошибках БД
     */
    long addNewAccount(Account account) throws DAOException;

    /**
     * Получает все счета клиента
     * @param clientId - id клиента в БД
     * @return список List<Account> всех счетов клиента
     * @throws DAOException если клиента с таким id в базе не существует и при ошибках БД
     */
    List<Account> getAllAccountsByClientId(long clientId) throws DAOException;

    /**
     * Получает счет клиента
     * @param accountId - id счета в БД
     * @return счет Account
     * @throws DAOException если счета с таким id в базе не существует и при ошибках БД
     */
    Account getAccountById(long accountId) throws DAOException;

    /**
     * Проверяет существует ли счет в БД
     * @param number - номер счета
     * @return boolean - true если счет существует
     * @throws DAOException при ошибках БД
     */
    boolean isAccountExists(String number) throws DAOException;

    /**
     * Добавляет средства на счет клиента
     * @param accountId - id счета в БД
     * @param funds - сумма к пополнению
     * @return boolean - true если пополнение прошло успешно
     * @throws DAOException при ошибках БД
     */
    boolean addFundsToAccount(long accountId, BigDecimal funds) throws DAOException;
}
