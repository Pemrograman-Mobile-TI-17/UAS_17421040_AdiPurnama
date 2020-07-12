package com.Adi.server;

public class BaseURL {

    public static String baseUrl = "http://192.168.43.180:5050/";

    public static String login      = baseUrl + "user/login";
    public static String register   = baseUrl + "user/registrasi";

    //Sneak
    public static String dataSnack = baseUrl + "snack/datasneak";
    public static String editDataSnack = baseUrl + "snack/ubah/";
    public static String hapusData = baseUrl + "snack/hapus/";
    public static String inputSnack = baseUrl + "snack/input";
}
