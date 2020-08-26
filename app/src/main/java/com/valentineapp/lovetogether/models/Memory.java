package com.valentineapp.lovetogether.models;

/**
 * Created by lequy on 2/3/2017.
 */

public class Memory {
    private int id;
    private String time;
    private String memoryName;
    private long days;
    private String image64;

    public Memory(String time, String memoryName, long days, String image64) {
        this.time = time;
        this.memoryName = memoryName;
        this.days = days;
        this.image64 = image64;
    }

    public Memory() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMemoryName() {
        return memoryName;
    }

    public void setMemoryName(String memoryName) {
        this.memoryName = memoryName;
    }

    public long getDays() {
        return days;
    }

    public void setDays(long days) {
        this.days = days;
    }

    public String getImage64() {
        return image64;
    }

    public void setImage64(String image64) {
        this.image64 = image64;
    }
}
