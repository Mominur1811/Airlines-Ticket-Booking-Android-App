package com.example.easybook_airlines;

import java.util.Date;

public class UserNotificationRetreiver {
    String FlightID,amount,message,account,ticket_count,from,to,depart,id;
    long acceptDate;

    public UserNotificationRetreiver() {
    }

    public UserNotificationRetreiver(String flightID, String amount, String message, String account, String ticket_count, String from, String to, String depart,String id,long acceptDate) {
        FlightID = flightID;
        this.amount = amount;
        this.message = message;
        this.account = account;
        this.ticket_count = ticket_count;
        this.from = from;
        this.to = to;
        this.depart = depart;
        this.id=id;
        this.acceptDate=acceptDate;
    }

    public String getFlightID() {
        return FlightID;
    }

    public void setFlightID(String flightID) {
        FlightID = flightID;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getTicket_count() {
        return ticket_count;
    }

    public void setTicket_count(String ticket_count) {
        this.ticket_count = ticket_count;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getDepart() {
        return depart;
    }

    public void setDepart(String depart) {
        this.depart = depart;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getAcceptDate() {
        return acceptDate;
    }

    public void setAcceptDate(long acceptDate) {
        this.acceptDate = acceptDate;
    }
}
