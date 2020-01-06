package com.example.myfyp;

public class URLs {
    private static final String ROOT_URL = "http://10.5.4.86/Admin/Api.php?apicall=";

    public static final String URL_REGISTER = ROOT_URL + "signup";
    public static final String URL_LOGIN= ROOT_URL + "login";
    public static final String URL_CREATE_ROOM = ROOT_URL + "createroom";
    public static final String URL_CREATE_ROOMMem = ROOT_URL + "createroommember";
    public static final String URL_CREATE_OffHOURS= ROOT_URL + "createofficehours";
    public static final String URL_CREATE_PIC = ROOT_URL + "createpicture";

    public static final String ROOT_URL2 = "http://10.5.4.86/Admin/RoomApi.php?apicall=";


    public static final String URL_READ_ROOM = ROOT_URL2 + "getrooms";
    public static final String URL_READ_STAFFMEM = ROOT_URL2 + "getstaffmem";
    public static final String URL_DELETE_HERO = ROOT_URL + "deleteroom&id=";

      
}
