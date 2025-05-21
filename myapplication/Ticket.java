package com.example.myapplication;

public class Ticket {
    
    private String zone;
    private int idPos;
    private String time;
    private String date;
    private boolean isPaid;

    public Ticket(String zone, int idPos, String time, String date, boolean isPaid) {
        this.zone = zone;
        this.idPos = idPos;
        this.time = time;
        this.date = date;
        this.isPaid = isPaid;
    }

}
