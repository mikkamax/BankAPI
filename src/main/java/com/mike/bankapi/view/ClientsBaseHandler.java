package com.mike.bankapi.view;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.mike.bankapi.controller.ClientController;
import com.mike.bankapi.model.dao.DAOException;
import com.mike.bankapi.service.Utils;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * Базовая имплементация Handler'а, отвечающая за обработку запросов API клиентов.
 * Наследники класса должны переопределить handleGetRequest и/или handlePostRequest для корректной обработки запросов
 * POST-запросы принимаются в формате JSON
 */
public class ClientsBaseHandler implements HttpHandler {
    protected ClientController clientController;

    /**
     * @param clientController - контроллер, через который все СlientsHadler'ы взаимодействуют со слоем ДАО
     */
    public ClientsBaseHandler(ClientController clientController) {
        this.clientController = clientController;
    }

    /**
     * Получает веб-запрос, делает его предварительную обработку, формирует и отправляет ответ.
     * Осуществляет обработку DAOException и HandlerException
     * @param exchange - запрос, поступивший от HttpServer'а
     */
    @Override
    public void handle(HttpExchange exchange) {
        StringBuilder sb;
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        try {
            if (exchange.getRequestMethod().equals("GET")) {
                Map<String, String> queryParams = queryToMap(exchange.getRequestURI().getQuery());
                sb = handleGetRequest(exchange, queryParams, mapper);
            } else if (exchange.getRequestMethod().equals("POST")) {
                try {
                    JsonNode jsonNode = new ObjectMapper().readTree(exchange.getRequestBody());
                    sb = handlePostRequest(exchange, jsonNode, mapper);
                } catch (IOException e) {
                    String error = "Ошибка! Не удалось получить данные POST-запроса";
                    Utils.printMessage(error);
                    throw new HandlerException(error, e);
                }
            } else {
                throw new HandlerException("В текущей версии API поддерживаются только POST и GET запросы");
            }
        } catch (HandlerException|DAOException e) {
            e.printStackTrace();
            sb = new StringBuilder();
            sb.append("{")
                    .append(System.lineSeparator())
                    .append("  \"error\": \"")
                    .append(e.getMessage())
                    .append("\"")
                    .append(System.lineSeparator())
                    .append("}");
        }

        try {
            byte[] bytes = sb.toString().getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().set("Content-Type", "application/json; charset=" + StandardCharsets.UTF_8);
            exchange.sendResponseHeaders(200, bytes.length);
            OutputStream os = exchange.getResponseBody();
            os.write(bytes);
            os.flush();
            os.close();
        } catch (IOException e) {
            String error = "Ошибка с формированием/отправкой ответа";
            Utils.printMessage(error);
            e.printStackTrace();
        }
    }

    /**
     * Хэндлер-наследник, обрабатывающий GET-запросы, должен переопределить данный метод
     * @param exchange - запрос, поступивший от HttpServer'а
     * @param queryParams - параметры запроса в формате ключ-значение
     * @param mapper - объект фреймфорка JackSon, который используется для сериализации ответа в JSON-формат
     * @return StringBuilder - готовый к отправке ответ
     * @throws HandlerException
     * @throws DAOException
     */
    protected StringBuilder handleGetRequest(HttpExchange exchange, Map<String, String> queryParams, ObjectMapper mapper) throws HandlerException, DAOException {
        String error = "Этот API не принимает GET-запросы";
        throw new HandlerException(error);
    }

    /**
     * Хэндлер-наследник, обрабатывающий POST-запросы, должен переопределить данный метод
     * @param exchange - запрос, поступивший от HttpServer'а
     * @param jsonNode - параметры запроса в формате ключ-значение
     * @param mapper - объект фреймфорка JackSon, который используется для сериализации ответа в JSON-формат
     * @return StringBuilder - готовый к отправке ответ
     * @throws HandlerException
     * @throws DAOException
     */
    protected StringBuilder handlePostRequest(HttpExchange exchange, JsonNode jsonNode, ObjectMapper mapper) throws HandlerException, DAOException {
        String error = "Этот API не принимает POST-запросы";
        throw new HandlerException(error);
    }

    /**
     * Утилитный метод для преобразования параметров GET-запроса в Map в формате ключ-значение
     * @param query параметры GET-запроса
     * @return Map<String, String> с параметрами запроса в виде ключ-значение
     */
    private Map<String, String> queryToMap(String query) {
        Map<String, String> queryMap = new HashMap<>();
        if (query != null) {
            String[] queries = query.split("&");
            for (String que : queries) {
                int dividerIndex = que.indexOf("=");
                if (dividerIndex != -1) {
                    queryMap.put(que.substring(0, dividerIndex), que.substring(dividerIndex + 1));
                }
            }
        }
        return queryMap;
    }
}
