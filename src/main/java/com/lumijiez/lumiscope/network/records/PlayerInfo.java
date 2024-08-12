package com.lumijiez.lumiscope.network.records;

public class PlayerInfo {
    public String name;
    public double direction;
    public double distance;

    public PlayerInfo(String name, double direction, double distance) {
        this.name = name;
        this.direction = direction;
        this.distance = distance;
    }

    public PlayerInfo(double direction) {
        this.name = "none";
        this.direction = direction;
        this.distance = 0;
    }
}