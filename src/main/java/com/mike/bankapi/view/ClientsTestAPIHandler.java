package com.mike.bankapi.view;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mike.bankapi.controller.ClientController;
import com.mike.bankapi.model.dao.DAOException;
import com.mike.bankapi.model.entity.Client;
import com.mike.bankapi.service.Utils;
import com.sun.net.httpserver.HttpExchange;

import java.util.List;

public class ClientsTestAPIHandler extends ClientsBaseHandler {
    public ClientsTestAPIHandler(ClientController clientController) {
        super(clientController);
    }

    @Override
    protected StringBuilder handleGetRequest(HttpExchange exchange, String query, ObjectMapper mapper) throws HandlerException, DAOException {
        try {
            StringBuilder sb = new StringBuilder();
            List<Client> clientList = clientController.getAllData();
            sb.append(mapper.writeValueAsString(clientList));
            return sb;
        } catch (JsonProcessingException e) {
            String error = "Ошибка при json-сериализации всех пользователей";
            Utils.printMessage(error);
            throw new HandlerException(error, e);
        }
    }
}
