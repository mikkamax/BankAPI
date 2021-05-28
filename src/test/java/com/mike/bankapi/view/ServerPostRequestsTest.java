package com.mike.bankapi.view;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.mike.bankapi.controller.ClientController;
import com.mike.bankapi.model.dao.DAOException;
import com.mike.bankapi.model.dao.DAOFactory;
import com.mike.bankapi.service.HttpWebServer;
import com.mike.bankapi.service.ServiceException;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;

public class ServerPostRequestsTest {
    public static String urlMain = "http://" + HttpWebServer.SERVER_HOSTNAME + ":" + HttpWebServer.SERVER_PORT;
    public static String urlAddFunds = urlMain + "/clients/add_funds/";
    public static String urlNewCard = urlMain + "/clients/new_card/";

    private static ClientController clientController;
    private static ObjectMapper mapper;
    private static HttpWebServer httpWebServer;

    @BeforeClass
    public static void mainInit() throws DAOException, ServiceException {
        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.H2_DB);
        daoFactory.initDb();

        clientController = new ClientController(daoFactory);

        httpWebServer = new HttpWebServer(clientController);
        httpWebServer.start();

        mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @AfterClass
    public static void after() {
        httpWebServer.stop();
    }

    @Test
    public void addFunds() throws IOException, DAOException {
        String url = urlAddFunds;
        String outgoingRequest = "{\"account_id\":10,\"funds\":1000.00}";
        JsonNode actual = actualResponse(url, outgoingRequest);

        String expectedString = "{\"result\":\"true\"}";
        JsonNode expected = mapper.readTree(expectedString);

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void addFundsFailOnClientId() throws IOException, DAOException {
        String url = urlAddFunds;
        String outgoingRequest = "{\"account_id\":20,\"funds\":1000.00}";
        JsonNode actual = actualResponse(url, outgoingRequest);

        String expectedString = "{\"error\": \"Ошибка! Не удалось получить счет по Id\"}";
        JsonNode expected = mapper.readTree(expectedString);

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void addFundsFailOnLackOfData() throws IOException, DAOException {
        String url = urlAddFunds;
        String outgoingRequest = "{\"account_id\":20}";
        JsonNode actual = actualResponse(url, outgoingRequest);

        String expectedString = "{\"error\": \"Ошибка! Недостаточно входящих данных для обработки POST-запроса\"}";
        JsonNode expected = mapper.readTree(expectedString);

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void addFundsFailOnNegativeAmount() throws IOException, DAOException {
        String url = urlAddFunds;
        String outgoingRequest = "{\"account_id\":10,\"funds\":-10.00}";
        JsonNode actual = actualResponse(url, outgoingRequest);

        String expectedString = "{\"error\":\"Ошибка! Сумма для пополнения должна быть положительной\"}";
        JsonNode expected = mapper.readTree(expectedString);

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void createNewCard() throws IOException, DAOException {
        String url = urlNewCard;
        String outgoingRequest = "{\"client_id\":10,\"account_id\":-1,\"card_limit\":100.00}";
        JsonNode actual = actualResponse(url, outgoingRequest);

        String expectedString = "{\"result\":\"true\"}";
        JsonNode expected = mapper.readTree(expectedString);

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void createNewCardFailOnClientId() throws IOException, DAOException {
        String url = urlNewCard;
        String outgoingRequest = "{\"client_id\":0,\"account_id\":-1,\"card_limit\":100.00}";
        JsonNode actual = actualResponse(url, outgoingRequest);

        String expectedString = "{\"error\":\"Ошибка! Не удалось получить клиента по Id\"}";
        JsonNode expected = mapper.readTree(expectedString);

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void createNewCardFailOnLackOfData() throws IOException, DAOException {
        String url = urlNewCard;
        String outgoingRequest = "{\"client_id\":10,\"account_id\":1,\"card_limit\":100.00}";
        JsonNode actual = actualResponse(url, outgoingRequest);

        String expectedString = "{\"error\":\"Переданный id счета не соответствует переданному id клиента\"}";
        JsonNode expected = mapper.readTree(expectedString);

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void createNewCardFailOnClientIdNotMatchAccountId() throws IOException, DAOException {
        String url = urlNewCard;
        String outgoingRequest = "{\"card_limit\":100.00}";
        JsonNode actual = actualResponse(url, outgoingRequest);

        String expectedString = "{\"error\":\"Ошибка! Недостаточно входящих данных для обработки POST-запроса\"}";
        JsonNode expected = mapper.readTree(expectedString);

        Assert.assertEquals(expected, actual);
    }

    private JsonNode actualResponse(String urlToGet, String outgoing) throws IOException {
        URL url = new URL(urlToGet);
        URLConnection connection = url.openConnection();
        connection.setDoOutput(true);

        OutputStreamWriter osw = new OutputStreamWriter(connection.getOutputStream());
        osw.write(outgoing);
        osw.flush();
        osw.close();

        JsonNode jsonNode = mapper.readTree(connection.getInputStream());

        return jsonNode;
    }
}