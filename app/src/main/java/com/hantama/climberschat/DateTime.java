package com.hantama.climberschat;

import java.text.DateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.text.DateFormat.getDateTimeInstance;


public class DateTime {
    private long time;

    public DateTime(){

    }
    public DateTime(String time) {
        this.time =Long.parseLong(time);
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }



    public String getTimeDate(){
        try{
            DateFormat dateFormat = getDateTimeInstance();
            Date netDate = (new Date(time));
            return dateFormat.format(netDate);
        } catch(Exception e) {
            return "date";
        }
    }

    public String getJam(){
        String a=this.getTimeDate();
//        Pattern p = Pattern.compile("\\d{2}\\:\\d{2}");
//        Matcher m = p.matcher(a);


            String b=a.substring(a.length()-11,a.length()-6)+a.substring(a.length()-3);

            return b;
       // else return "date";

    }
}
