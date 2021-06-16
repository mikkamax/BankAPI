package com.mike.bankapi.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mike.bankapi.service.ClientService;
import com.mike.bankapi.model.dao.DAOException;
import com.mike.bankapi.service.Utils;
import com.sun.net.httpserver.HttpExchange;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Хэндлер, обрабатывающий POST-запросы по пополнению счета
 */
public class ClientsAddFundsHandler extends ClientsBaseHandler {
    public ClientsAddFundsHandler(ClientService clientService) {
        super(clientService);
    }

    @Override
    protected StringBuilder handlePostRequest(HttpExchange exchange, JsonNode jsonNode, ObjectMapper mapper) throws HandlerException, DAOException {
        try {
            StringBuilder sb = new StringBuilder();

            if (!jsonNode.has("account_id") || !jsonNode.has("funds")) {
                String error = "Ошибка! Недостаточно входящих данных для обработки POST-запроса";
                Utils.printMessage(error);
                throw new HandlerException(error);
            }

            long accountId = jsonNode.get("account_id").asLong();
            BigDecimal fundsToAdd = new BigDecimal(jsonNode.get("funds").asText());

            if (fundsToAdd.compareTo(new BigDecimal(0)) <= 0) {
                String error = "Ошибка! Сумма для пополнения должна быть положительной";
                Utils.printMessage(error);
                throw new HandlerException(error);
            }

            boolean result = clientService.addFundsToAccount(accountId, fundsToAdd);
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
