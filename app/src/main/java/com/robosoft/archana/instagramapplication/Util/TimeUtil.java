package com.robosoft.archana.instagramapplication.Util;

/**
 * Created by archana on 25/4/16.
 */
public class TimeUtil {

    public static String convertMilliSecToYMD(String date) {

        long currentTime = Long.parseLong(date);
        long now = System.currentTimeMillis();
        long timeDiff = now - currentTime*1000;
        long second = timeDiff / 1000;
        long minute=0,hour=0,days=0,week,month=0,year=0;
        if(second>=60){
            minute = second/60;
        }
        if(minute>=60){
            hour = minute/60;
        }
        if(hour>=24){
            days = hour/24;
        }
        if(days>=30){
            month = days/30;
        }
        if(month>=12){
            year = month/12;
        }
        if(year>=1){
            return year+"y";
        }else if(month>=1&&month<12){
            if(month==1){
                return month+"month";
            }
            return month+"months";
        }else  if(days>=1&&days<7){
            return days+"d";
        }else if(days>=7&&days<29){
            week = days/7;
            return week+"w";
        }else if(hour<24&&hour>minute){
            return hour+"h";
        }else if(minute<60){
            return minute+"m";
        }else {
            return second+"s";
        }

    }
}





