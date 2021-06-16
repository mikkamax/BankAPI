package com.mike.bankapi.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mike.bankapi.service.ClientService;
import com.mike.bankapi.model.dao.DAOException;
import com.mike.bankapi.model.entity.Card;
import com.mike.bankapi.service.Utils;
import com.sun.net.httpserver.HttpExchange;

import java.util.List;
import java.util.Map;

/**
 * Хэндлер, обрабатывающий GET-запросы по выводу списка всех карт клиента
 */
public class ClientsCardsHandler extends ClientsBaseHandler {
    public ClientsCardsHandler(ClientService clientService) {
        super(clientService);
    }

    @Override
    protected StringBuilder handleGetRequest(HttpExchange exchange, Map<String, String> queryParams, ObjectMapper mapper) throws HandlerException, DAOException {
        try {
            long incomingLong = Long.parseLong(queryParams.get("client_id"));
            StringBuilder sb = new StringBuilder();
            List<Card> cardList = clientService.getAllClientCards(incomingLong);
            sb.append(mapper.writeValueAsString(cardList));
            return sb;
        } catch (NumberFormatException e) {
            String error = "Ошибка! Неверные входные данные в запросе";
            Utils.printMessage(error);
            throw new HandlerException(error, e);
        } catch (JsonProcessingException e) {
            String error = "Ошибка при json-сериализации карт пользователей";
            Utils.printMessage(error);
            throw new HandlerException(error, e);
        }
    }
}
