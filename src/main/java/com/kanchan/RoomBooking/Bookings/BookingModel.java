package com.kanchan.RoomBooking.Bookings;

import com.kanchan.RoomBooking.Rooms.RoomModel;
import com.kanchan.RoomBooking.Users.UserModel;
import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "bookings")
public class BookingModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ManyToOne(targetEntity = UserModel.class)
    @JoinColumn(name = "user_id")
    private UserModel user;

    @ManyToOne(targetEntity = RoomModel.class)
    @JoinColumn(name = "room_id")
    private RoomModel room;

    @Column(name = "dateOfBooking")
    private Date dateOfBooking;

    @Column(name = "timeFrom")
    private Date timeFrom;

    @Column(name = "timeTo")
    private Date timeTo;

    @Column(name = "purpose")
    private String purpose;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public RoomModel getRoom() {
        return room;
    }

    public void setRoom(RoomModel room) {
        this.room = room;
    }

    public Date getDateOfBooking() {
        return dateOfBooking;
    }

    public void setDateOfBooking(Date dateOfBooking) {
        this.dateOfBooking = dateOfBooking;
    }

    public Date getTimeFrom() {
        return timeFrom;
    }

    public void setTimeFrom(Date timeFrom) {
        this.timeFrom = timeFrom;
    }

    public Date getTimeTo() {
        return timeTo;
    }

    public void setTimeTo(Date timeTo) {
        this.timeTo = timeTo;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }
}
