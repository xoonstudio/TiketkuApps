package com.xoonstudio.tiketku;

public class TicketItem {
    String travel_name, travel_location, total_ticket, transaction_number, total_price;

    public TicketItem() {
    }

    public TicketItem(String travel_name, String travel_location, String total_ticket, String transaction_number, String total_price) {
        this.travel_name = travel_name;
        this.travel_location = travel_location;
        this.total_ticket = total_ticket;
        this.transaction_number = transaction_number;
        this.total_price = total_price;
    }

    public String getTravel_name() {
        return travel_name;
    }

    public void setTravel_name(String travel_name) {
        this.travel_name = travel_name;
    }

    public String getTravel_location() {
        return travel_location;
    }

    public void setTravel_location(String travel_location) {
        this.travel_location = travel_location;
    }

    public String getTotal_ticket() {
        return total_ticket;
    }

    public void setTotal_ticket(String total_ticket) {
        this.total_ticket = total_ticket;
    }

    public String getTransaction_number() {
        return transaction_number;
    }

    public void setTransaction_number(String transaction_number) {
        this.transaction_number = transaction_number;
    }

    public String getTotal_price() {
        return total_price;
    }

    public void setTotal_price(String total_price) {
        this.total_price = total_price;
    }
}
