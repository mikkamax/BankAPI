package com.mike.bankapi.model.dao;

import com.mike.bankapi.model.entity.Card;
import com.mike.bankapi.model.entity.Client;

import java.util.List;

/**
 * Интерфейс для работы с клиентами, наследники должны реализовать все методы
 */

public interface ClientDAO {
    /**
     * Получает всех клиентов из БД
     * @return список List<Client> всех клиентов в БД
     * @throws DAOException при ошибках БД
     */
    List<Client> getAllClients() throws DAOException;

    /**
     * Получает клиента из БД
     * @param clientId - id клиента в БД
     * @return клиента Client
     * @throws DAOException если клиента с таким id в базе не существует и при ошибках БД
     */
    Client getClientById(long clientId) throws DAOException;

    /**
     * Получает все карты клиента
     * @param clientId - id клиента в БД
     * @return список List<Card> всех карт клиента по всем счетам
     * @throws DAOException если клиента с таким id в базе не существует и при ошибках БД
     */
    List<Card> getAllClientCards(long clientId) throws DAOException;
}
