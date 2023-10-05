package com.skh.enjoyriding.model;

public class Hours {
    private int hourId;
    private int hour;

    public Hours(int hour) {
        this.hourId = 0;
        this.hour = hour;
    }

    public Hours(int hourId, int hour) {
        this.hourId = hourId;
        this.hour = hour;
    }

    public int getHourId() {
        return hourId;
    }

    public int getHour() {
        return hour;
    }
}
