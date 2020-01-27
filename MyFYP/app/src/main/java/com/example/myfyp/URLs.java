package com.example.myfyp;

public class URLs {
    private static final String ROOT_URL = "http://192.168.1.102/Admin/Api.php?apicall=";

    public static final String URL_REGISTER = ROOT_URL + "signup";
    public static final String URL_LOGIN= ROOT_URL + "login";
    public static final String URL_CREATE_ROOM = ROOT_URL + "createroom";
    public static final String URL_CREATE_ROOMMem = ROOT_URL + "createroommember";
    public static final String URL_CREATE_OffHOURS= ROOT_URL + "createofficehours";
    public static final String ROOM1 = ROOT_URL+"room1";
    public static final String ROOMMEM1 = ROOT_URL+"roommem1";
    public static final String OFFICEHOURS1 = ROOT_URL+"officehours1";

    public static final String ROOT_URL2 = "http://192.168.1.102/Admin/RoomApi.php?apicall=";

    public static final String URL_READ_ROOM = ROOT_URL2 + "getrooms";
    public static final String  URL_READ_TIMINGS = ROOT_URL2 + "gettiming";

    public static final String URL_READ_STAFFMEM = ROOT_URL2 + "getstaffmem";
    public static final String URL_DELETE_ROOM = ROOT_URL2 + "deleteroom&id=";
    public static final String URL_DELETE_ROOMMEM = ROOT_URL2 + "deleteroommembers&id=";
    public static final String URL_DELETE_TIMINGS = ROOT_URL2 + "deletetimings&id=";
    public static final String URL_UPDATE_ROOM = ROOT_URL2 + "updateroom";
    public static final String URL_UPDATE_ROOMMEM = ROOT_URL2 + "updateroommem";
    public static final String URL_UPDATE_TIMINGS = ROOT_URL2 + "updatetimings";

}
