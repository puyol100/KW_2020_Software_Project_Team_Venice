package com.example.sleep;

public class sleepytime_managing {
    static int hour= 0 , min= 0, am_pm= 0;

    //static String s1="",s2="",s3="",s4="";
    static String[] s = {"","","",""} ;
    public static void set_time(int h, int m, int ap)
    {
        hour = h;
        min = m;
        am_pm = ap;
    }
    public static String get_time()
    {
        String result ="";
        if(hour == 0 || min ==0)
        {
            result = "sleepytime을 먼저 설정 해 주세요.";
        }
        else {
            cal();
            result = s[3] + " or\n" + s[2] + " or\n" + s[1] + " or\n" + s[0];
        }
        return result;
    }

    public static void cal()
    {

        int temp_hour = hour - 4;
        int temp_min = min - 30;
        for(int i=0; i<4; i++){
            if (temp_hour < 0)
            {
                temp_hour = 12 + temp_hour;
                if (am_pm == 0)  am_pm = 1;
                else if (am_pm == 1) am_pm = 0;
            }
            if (temp_min < 0)
            {
                temp_hour = temp_hour - 1;
                temp_min = 60 + (temp_min);
                if (temp_hour < 0)
                {
                    if (am_pm == 0) am_pm = 1;
                    else if (am_pm == 1) am_pm = 0;
                    temp_hour = temp_hour + 12;
                }
            }
            if(am_pm ==0) {
                if(temp_min ==0)
                {
                    if(temp_hour == 0){
                        s[i] = "00: 00 AM";
                    }
                    else {
                        if(temp_hour - 10 <0){
                            s[i] = "0" +""+ temp_hour +":00 AM";
                        }
                        else {
                            s[i] = "" + temp_hour + ":00 AM";
                        }
                    }
                }
                else {
                    if(temp_hour - 10 <0){
                        if(temp_min - 10<0){
                            s[i] = "0" +""+temp_hour+":0"+""+temp_min +" AM";
                        }
                        else{
                            s[i] = "0" +""+temp_hour+":"+""+temp_min+" AM";
                        }
                    }
                    else{
                        if(temp_min -10 <0){
                            s[i] = ""+temp_hour+":0"+""+temp_min+" AM";
                        }
                        else{
                            s[i] = "" + temp_hour + ":" + "" + temp_min + " AM";
                        }
                    }
                }
            }
            else
            {
                if(temp_min ==0)
                {
                    if(temp_hour == 0){
                        s[i] = "00: 00 PM";
                    }
                    else {
                        if(temp_hour - 10 <0){
                            s[i] = "0" +"" + temp_hour + ":00 PM";
                        }
                        else {
                            s[i] = "" + temp_hour + ":00 PM";
                        }
                    }
                }
                else {
                    if(temp_hour - 10 <0){
                        if(temp_min - 10<0){
                            s[i] = "0" +""+temp_hour+":0"+""+temp_min +" PM";
                        }
                        else{
                            s[i] = "0" +""+temp_hour+":"+""+temp_min+" PM";
                        }
                    }
                    else{
                        if(temp_min -10 <0){
                            s[i] = ""+temp_hour+":0"+""+temp_min+" PM";
                        }
                        else{
                            s[i] = "" + temp_hour + ":" + "" + temp_min + " PM";
                        }
                    }
                }
            }
            temp_hour = temp_hour - 1;
            temp_min = temp_min - 30;
        }
    }
    public static void init()
    {
        hour = 0;
        min = 0;
        am_pm = 0;
        s[0] = "";
        s[1] = "";
        s[2] = "";
        s[3] = "";
    }
}