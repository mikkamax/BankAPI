package com.mike.bankapi.model.dao;

import com.mike.bankapi.model.entity.Card;

import java.util.List;

/**
 * Интерфейс для работы с картами, наследники должны реализовать все методы
 */

public interface CardDAO {

    /**
     * Добавляет созданную карту в БД
     * @param card объект карты для сохранения в БД
     * @return long - id созданной карты в БД
     * @throws DAOException при ошибке БД
     */
    long addNewCard(Card card) throws DAOException;

    /**
     * Получает все карты по счету
     * @param accountId - id счета в БД
     * @return список List<Card> всех карт по счету
     * @throws DAOException если клиента с таким id в базе не существует и при ошибках БД
     */
    List<Card> getAllCardsByAccountId(long accountId) throws DAOException;

    /**
     * Получает карту клиента
     * @param cardId - id карты в БД
     * @return карту Card
     * @throws DAOException если карты с таким id в базе не существует и при ошибках БД
     */
    Card getCardById(long cardId) throws DAOException;

    /**
     * Проверяет существует ли карта в БД
     * @param number - номер карты
     * @return boolean - true если карта существует
     * @throws DAOException при ошибках БД
     */
    boolean isCardExists(String number) throws DAOException;
}
