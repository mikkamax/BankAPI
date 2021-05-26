package com.mike.bankapi.model.dao;

import com.mike.bankapi.model.entity.Card;
import com.mike.bankapi.model.entity.Client;

import java.util.List;

public interface ClientDAO {
    public List<Client> getAllClients() throws DAOException;
    public Client getClientById(long clientId) throws DAOException;
    public List<Card> getAllClientCards(long clientId) throws DAOException;
}
