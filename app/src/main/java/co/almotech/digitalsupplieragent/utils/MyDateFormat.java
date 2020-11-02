package co.almotech.digitalsupplieragent.utils;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MyDateFormat {

    @SuppressLint("SimpleDateFormat")
    public static String getFormattedDate(String date)
    {
        String parsedDate = null;
        try{
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            Date dateObj = df.parse(date);
            parsedDate =  new SimpleDateFormat("EEE, d MMM yyyy").format(dateObj);
        }catch (ParseException e){
            e.printStackTrace();
        }

        return parsedDate;

    }

    @SuppressLint("SimpleDateFormat")
    public static String shortDate(String date){

        String parsedDate = null;
        try{
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            Date dateObj = df.parse(date);
            parsedDate =  new SimpleDateFormat("d MMM").format(dateObj);

        }catch (ParseException e){
            e.printStackTrace();
        }


        return parsedDate!= null ?  parsedDate.replace(" ", "\n") : date;

    }

}
