package com.ra.ayushma;

/**
 * Created by Admin on 21-02-2017.
 */
public class Doctor {
    public final String id;
    public final String name;
    public final String spec;
    public final String loc;
    public final String rating;
    public final String fee;

    public Doctor(String id, String name, String spec, String loc, String rating, String fee) {
        this.id = id;
        this.name = name;
        this.spec = spec;
        this.loc = loc;
        this.rating = rating;
        this.fee = fee;
    }

}
