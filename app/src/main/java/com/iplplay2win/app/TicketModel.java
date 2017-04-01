package com.iplplay2win.app;

/**
 * Created by Suraj Singh on 3/28/2017.
 */

public class TicketModel {
    String name, phone, iwant, ihave;
    String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public String getIwant() {
        return iwant;
    }

    public void setIwant(String iwant) {
        this.iwant = iwant;
    }

    public String getIhave() {
        return ihave;
    }

    public void setIhave(String ihave) {
        this.ihave = ihave;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}

