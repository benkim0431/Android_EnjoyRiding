package com.skh.enjoyriding.model;

public class Booking {

    private int bookingId;
    private int bikeId;
    private int userId;
    private int startHour;

    public Booking(int userId, int bikeId,  int startHour) {
        this.bookingId = 0;
        this.bikeId = bikeId;
        this.userId = userId;
        this.startHour = startHour;
    }

    public Booking(int bookingId, int userId, int bikeId, int startHour) {
        this.bookingId = bookingId;
        this.bikeId = bikeId;
        this.userId = userId;
        this.startHour = startHour;
    }

    public int getBookingId() {
        return bookingId;
    }

    public int getBikeId() {
        return bikeId;
    }

    public int getUserId() {
        return userId;
    }

    public int getStartHour() {
        return startHour;
    }

    public String validate(){
        String message = null;

        return message;
    }
}
