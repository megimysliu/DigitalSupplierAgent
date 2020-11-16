package co.almotech.digitalsupplieragent.utils;

public class Urls {

    static final String BASE_URL = "https://develop.almotech.co/digital_supplier/public/storage/";
    public static String getUrl(String url){
        return BASE_URL + url;
    }
}
