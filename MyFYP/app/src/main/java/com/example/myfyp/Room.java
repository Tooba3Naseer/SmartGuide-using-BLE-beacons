package com.example.myfyp;

public class Room {
    private int id;
    private String name, Roomno, desc;

    public Room(int id, String name, String Roomno,String desc) {
        this.id = id;
        this.name = name;
        this.Roomno = Roomno;
        this.desc = desc;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getRoomno() {
        return Roomno;

    }
    public void setRoomno(String RoomNo) {
        this.Roomno = RoomNo;
    }

    public String getDesc() {
        return desc;
    }
    public void setDesc(String desc) {
        this.desc = desc;
    }

}