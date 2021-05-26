package com.mike.bankapi.model.entity;

import java.sql.Date;
import java.util.List;
import java.util.Objects;

public class Client {
    private long clientId;
    private String lastName;
    private String firstName;
    private String middleName;
    private Date dateOfBirth;
    private String passportNum;
    private List<Account> accountList;

    public Client() {
    }

    public long getClientId() {
        return clientId;
    }

    public void setClientId(long clientId) {
        this.clientId = clientId;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getPassportNum() {
        return passportNum;
    }

    public void setPassportNum(String passportNum) {
        this.passportNum = passportNum;
    }

    public List<Account> getAccountList() {
        return accountList;
    }

    public void setAccountList(List<Account> accountList) {
        this.accountList = accountList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return Objects.equals(lastName, client.lastName) && Objects.equals(firstName, client.firstName) && Objects.equals(middleName, client.middleName) && Objects.equals(dateOfBirth, client.dateOfBirth) && Objects.equals(passportNum, client.passportNum);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lastName, firstName, middleName, dateOfBirth, passportNum);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Client{");
        sb.append("clientId=").append(clientId);
        sb.append(", lastName='").append(lastName).append('\'');
        sb.append(", firstName='").append(firstName).append('\'');
        sb.append(", middleName='").append(middleName).append('\'');
        sb.append(", dateOfBirth=").append(dateOfBirth);
        sb.append(", passportNum='").append(passportNum).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
