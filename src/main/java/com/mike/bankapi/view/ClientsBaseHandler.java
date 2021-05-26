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

public class ClientsBaseHandler implements HttpHandler {
    protected ClientController clientController;

    public ClientsBaseHandler(ClientController clientController) {
        this.clientController = clientController;
    }

    @Override
    public void handle(HttpExchange exchange) {
        StringBuilder sb = null;
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        try {
            if (exchange.getRequestMethod().equals("GET")) {
                String query = exchange.getRequestURI().toString();

                if (query.charAt(query.length() - 1) == '/') {
                    query = query.substring(0, query.length() - 1).toLowerCase();
                }

                sb = handleGetRequest(exchange, query, mapper);
            } else if (exchange.getRequestMethod().equals("POST")) {
                try {
                    JsonNode jsonNode = new ObjectMapper().readTree(exchange.getRequestBody());
                    sb = handlePostRequest(exchange, jsonNode, mapper);
                } catch (IOException e) {
                    String error = "Ошибка! Не удалось получить данные POST-запроса";
                    Utils.printMessage(error);
                    throw new HandlerException(error, e);
                }
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
            OutputStream os = null;
            byte[] bytes = sb.toString().getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().set("Content-Type", "application/json; charset=" + StandardCharsets.UTF_8);
            exchange.sendResponseHeaders(200, bytes.length);
            os = exchange.getResponseBody();
            os.write(bytes);
            os.flush();
            os.close();
        } catch (IOException e) {
            String error = "Ошибка с формированием/отправкой ответа";
            Utils.printMessage(error);
            e.printStackTrace();
        }
    }

    protected StringBuilder handleGetRequest(HttpExchange exchange, String query, ObjectMapper mapper) throws HandlerException, DAOException {
        String error = "Этот API не принимает GET-запросы";
        throw new HandlerException(error);
    }

    protected StringBuilder handlePostRequest(HttpExchange exchange, JsonNode jsonNode, ObjectMapper mapper) throws HandlerException, DAOException {
        String error = "Этот API не принимает POST-запросы";
        throw new HandlerException(error);
    }
}
