package com.mike.bankapi.view;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mike.bankapi.controller.ClientController;
import com.mike.bankapi.model.dao.DAOException;
import com.mike.bankapi.model.entity.Account;
import com.mike.bankapi.service.Utils;
import com.sun.net.httpserver.HttpExchange;

import java.util.Map;

public class ClientsBalanceHandler extends ClientsBaseHandler {
    public ClientsBalanceHandler(ClientController clientController) {
        super(clientController);
    }

    @Override
    protected StringBuilder handleGetRequest(HttpExchange exchange, Map<String, String> queryParams, ObjectMapper mapper) throws HandlerException, DAOException {
        try {
            long incomingLong = Long.parseLong(queryParams.get("account_id"));
            StringBuilder sb = new StringBuilder();
            Account account = clientController.getAccountById(incomingLong);
            sb.append(mapper.writeValueAsString(account));
            return sb;
        } catch (NumberFormatException e) {
            String error = "Ошибка! Неверные входные данные в запросе";
            Utils.printMessage(error);
            throw new HandlerException(error, e);
        } catch (JsonProcessingException e) {
            String error = "Ошибка при json-сериализации данных клиентского счета";
            Utils.printMessage(error);
            throw new HandlerException(error, e);
        }
    }
}
