package com.example.myfyp;

public class RoomStaffMems {
    private int id;
    private String Pname, Roomno, desg, experts;

    public RoomStaffMems(int id, String Pname, String Roomno,String desg, String experts) {
        this.id = id;
        this.Pname = Pname;
        this.Roomno = Roomno;
        this.desg = desg;
        this.experts = experts;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }


    public String getName() {
        return Pname;
    }
    public void setName(String name) {
        this.Pname = name;
    }


    public String getRoomno() { return Roomno; }
    public void setRoomno(String RoomNo) {
        this.Roomno = RoomNo;
    }

    public String getDesg() {
        return desg;
    }
    public void setDesg(String desg) {
        this.desg = desg;
    }


    public String getExperts() {
        return experts;
    }
    public void setExperts(String experts) {
        this.experts = experts;
    }

}
