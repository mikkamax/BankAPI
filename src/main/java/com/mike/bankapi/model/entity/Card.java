package com.mike.bankapi.model.entity;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Класс карты клиента, переменная cardId не участвует в equals и hashcode
 */
public class Card {
    private long cardId;
    private long accountId;
    private String cardNumber;
    private BigDecimal dailyLimit;

    public Card() {
    }

    public long getCardId() {
        return cardId;
    }

    public void setCardId(long cardId) {
        this.cardId = cardId;
    }

    public long getAccountId() {
        return accountId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public BigDecimal getDailyLimit() {
        return dailyLimit;
    }

    public void setDailyLimit(BigDecimal dailyLimit) {
        this.dailyLimit = dailyLimit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return Objects.equals(accountId, card.accountId) && Objects.equals(cardNumber, card.cardNumber) && Objects.equals(dailyLimit, card.dailyLimit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountId, cardNumber, dailyLimit);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Card{");
        sb.append("cardId=").append(cardId);
        sb.append(", account=").append(accountId);
        sb.append(", cardNumber=").append(cardNumber);
        sb.append(", dailyLimit=").append(dailyLimit);
        sb.append('}');
        return sb.toString();
    }
}
