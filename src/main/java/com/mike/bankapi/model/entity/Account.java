package com.mike.bankapi.model.entity;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

public class Account {
    private long accountId;
    private long clientId;
    private String number;
    private BigDecimal balance;
    private List<Card> cardList;

    public Account() {
    }

    public long getAccountId() {
        return accountId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }

    public long getClientId() {
        return clientId;
    }

    public void setClientId(long clientId) {
        this.clientId = clientId;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public List<Card> getCardList() {
        return cardList;
    }

    public void setCardList(List<Card> cardList) {
        this.cardList = cardList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return Objects.equals(clientId, account.clientId) && Objects.equals(number, account.number) && Objects.equals(balance, account.balance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clientId, number, balance);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Account{");
        sb.append("clientId=").append(clientId);
        sb.append(", client=").append(clientId);
        sb.append(", number=").append(number);
        sb.append(", balance=").append(balance);
        sb.append('}');
        return sb.toString();
    }
}
