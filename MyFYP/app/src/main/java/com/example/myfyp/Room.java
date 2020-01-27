package com.example.myfyp;

public class Room {
    private static int id;
    private static String name, Roomno, desc, LeftRoomno , RightRoomno ,FrontRoomno ,BackRoomno;

    public Room(int id, String name, String Roomno,String desc) {
        this.id = id;
        this.name = name;
        this.Roomno = Roomno;
        this.desc = desc;
    }

    public static int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }


    public static String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public static String getRoomno() {
        return Roomno;

    }
    public void setRoomno(String RoomNo) {
        this.Roomno = RoomNo;
    }
    public static String getDesc() {
        return desc;
    }
    public void setDesc(String desc) {
        this.desc = desc;
    }
    public static String getLeftRoomno() {
        return LeftRoomno;

    }
    public static String getRightRoomno() {
        return RightRoomno;

    }
    public static String getFrontRoomno() {
        return FrontRoomno;

    }
    public static String getBackRoomno() {
        return BackRoomno;

    }

}