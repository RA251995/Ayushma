package com.ra.ayushma;

/**
 * Created by Admin on 25-02-2017.
 */

public class Booking {
    public final String book_id;
    public final String doctor_id;
    public final String date_time;
    public final String doctor_loc;
    public final String doctor_name;

    public Booking(String book_id, String doctor_id, String date_time, String doctor_loc, String doctor_name) {
        this.book_id=book_id;
        this.doctor_id=doctor_id;
        this.date_time=date_time;
        this.doctor_loc=doctor_loc;
        this.doctor_name=doctor_name;
    }
}
