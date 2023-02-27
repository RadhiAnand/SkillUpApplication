package com.softura.skillup.entity;

public class Window {
    private boolean DriverSeat;
    private boolean PassengerSeat;
    private boolean leftSeat;

    public boolean isDriverSeat() {
        return DriverSeat;
    }

    public void setDriverSeat(boolean driverSeat) {
        DriverSeat = driverSeat;
    }

    public boolean isPassengerSeat() {
        return PassengerSeat;
    }

    public void setPassengerSeat(boolean passengerSeat) {
        PassengerSeat = passengerSeat;
    }

    public boolean isLeftSeat() {
        return leftSeat;
    }

    public void setLeftSeat(boolean leftSeat) {
        this.leftSeat = leftSeat;
    }
}
