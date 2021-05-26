package com.mike.bankapi.view;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mike.bankapi.controller.ClientController;
import com.mike.bankapi.model.dao.DAOException;
import com.mike.bankapi.service.Utils;
import com.sun.net.httpserver.HttpExchange;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class ClientsAddFundsHandler extends ClientsBaseHandler {
    public ClientsAddFundsHandler(ClientController clientController) {
        super(clientController);
    }

    @Override
    protected StringBuilder handlePostRequest(HttpExchange exchange, JsonNode jsonNode, ObjectMapper mapper) throws HandlerException, DAOException {
        try {
            StringBuilder sb = new StringBuilder();

            long accountId = jsonNode.get("account_id").asLong();
            BigDecimal fundsToAdd = new BigDecimal(jsonNode.get("funds").asText());
            boolean result = clientController.addFundsToAccount(accountId, fundsToAdd);
            Map<String, String> map = new HashMap<>();
            map.put("result", String.valueOf(result));
            sb.append(mapper.writeValueAsString(map));
            return sb;
        } catch (JsonProcessingException e) {
            String error = "Ошибка при json-сериализации ответа по созданию карты";
            Utils.printMessage(error);
            throw new HandlerException(error, e);
        }
    }
}
