package com.example.easybook_airlines;

public class Post {
    String flightId,from,to,seat,arrivalDate,price,query,time;

    public Post() {
    }

    public Post(String flightId, String from, String to, String seat, String arrivalDate, String price, String query, String time) {
        this.flightId = flightId;
        this.from = from;
        this.to = to;
        this.seat = seat;
        this.arrivalDate = arrivalDate;
        this.price = price;
        this.query = query;
        this.time = time;
    }

    @Override
    public String toString() {
        return "Post{" +
                "flightId='" + flightId + '\'' +
                ", from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", seat='" + seat + '\'' +
                ", arrivalDate='" + arrivalDate + '\'' +
                ", price='" + price + '\'' +
                ", query='" + query + '\'' +
                ", time='" + time + '\'' +
                '}';
    }

    public String getFlightId() {
        return flightId;
    }

    public void setFlightId(String flightId) {
        this.flightId = flightId;
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

    public String getSeat() {
        return seat;
    }

    public void setSeat(String seat) {
        this.seat = seat;
    }

    public String getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(String arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
