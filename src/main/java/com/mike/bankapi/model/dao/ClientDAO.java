package com.mike.bankapi.model.dao;

import com.mike.bankapi.model.entity.Card;
import com.mike.bankapi.model.entity.Client;

import java.util.List;

public interface ClientDAO {
    List<Client> getAllClients() throws DAOException;
    Client getClientById(long clientId) throws DAOException;
    List<Card> getAllClientCards(long clientId) throws DAOException;
}
