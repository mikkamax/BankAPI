package com.mike.bankapi.view;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mike.bankapi.controller.ClientController;
import com.mike.bankapi.model.dao.DAOException;
import com.mike.bankapi.model.entity.Client;
import com.mike.bankapi.service.Utils;
import com.sun.net.httpserver.HttpExchange;

import java.util.Map;

/**
 * Хэндлер, обрабатывающий GET-запросы по выводу всей информации о клиенте по его id в БД
 */
public class ClientsHandler extends ClientsBaseHandler {
    public ClientsHandler(ClientController clientController) {
        super(clientController);
    }

    @Override
    protected StringBuilder handleGetRequest(HttpExchange exchange, Map<String, String> queryParams, ObjectMapper mapper) throws HandlerException, DAOException {
        try {
            long incomingLong = Long.parseLong(queryParams.get("client_id"));
            StringBuilder sb = new StringBuilder();
            Client client = clientController.getAllClientDataById(incomingLong);
            sb.append(mapper.writeValueAsString(client));
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
