package com.example.easybook_airlines;

import java.io.Serializable;

public class Passenger implements Serializable {
    String passengerID,flightID,purchaseSeat,amount,account,from,to,depart,id;

    public Passenger() {
    }

    public Passenger(String passengerID, String flightID, String purchaseSeat, String amount, String account, String from, String to,String depart,String id) {
        this.passengerID = passengerID;
        this.flightID = flightID;
        this.purchaseSeat = purchaseSeat;
        this.amount = amount;
        this.account = account;
        this.from = from;
        this.to = to;
        this.depart=depart;
        this.id=id;
    }

    public String getPassengerID() {
        return passengerID;
    }

    public void setPassengerID(String passengerID) {
        this.passengerID = passengerID;
    }

    public String getFlightID() {
        return flightID;
    }

    public void setFlightID(String flightID) {
        this.flightID = flightID;
    }

    public String getPurchaseSeat() {
        return purchaseSeat;
    }

    public void setPurchaseSeat(String purchaseSeat) {
        this.purchaseSeat = purchaseSeat;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
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
}