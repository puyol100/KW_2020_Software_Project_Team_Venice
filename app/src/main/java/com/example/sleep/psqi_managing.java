package com.example.sleep;


public class psqi_managing {

    static int a1_hour= 0,a1_min=0, a2 =0, a3_hour =0,a3_min, a4 = 0;

    static int a5_a = 0, a5_b = 0, a5_c = 0;
    static int a5_d = 0, a5_e = 0, a5_f = 0;
    static int a5_g = 0, a5_h = 0, a5_i = 0;
    static int a6 = 0,a7 = 0,a8 = 0, a9 = 0, a10 = 0;
    static int a10_a = 0,a10_b = 0,a10_c = 0, a10_d = 0;

    static int result =0;

    public static void setq1_q2_a(int q1_hour,int q1_min, int q2){
            a1_hour = q1_hour;
            a1_min = q1_min;
            a2 = q2;
    }
    public static void setq3_q4_a(int q3_hour,int q3_min, int q4){
        a3_hour = q3_hour;
        a3_min = q3_min;
        a4 = q4;
    }
    public static void setq5_a_b_c(int a,int b,int c)
    {
        a5_a = a;
        a5_b = b;
        a5_c = c;
    }
    public static void setq5_d_e_f(int d,int e,int f)
    {
        a5_d = d;
        a5_e = e;
        a5_f = f;
    }
    public static void setq5_g_h_i(int g,int h,int i)
    {
        a5_g = g;
        a5_h = h;
        a5_i = i;
    }
    public static void setq6_q7_q8(int q6,int q7,int q8)
    {
        a6 = q6;
        a7 = q7;
        a8 = q8;
    }
    public static void setq9_q10(int q9,int q10)
    {
        a9 = q9;
        a10 = q10;
    }
    public static void setq10_a_b_c(int a,int b,int c)
    {
        a10_a = a;
        a10_b = b;
        a10_c = c;
    }
    public static void setq10_d(int d)
    {
        a10_d = d;
    }

    public static void record()
    {
        int component1 = 0,component2 = 0,component3=0, component4=0, component5=0,component6 = 0, component7 =0;
        component1 = a6-1;
        component2 = get_component2_record();
        component3 = get_component3_record();
        component4 = get_component4_record();
        component5 = get_component5_record();
        component6 = for_test(a7);
        component7 = get_component7_record();
        result = component1 + component2 + component3 + component4 + component5 + component6 + component7;
    }
    public static int get_component2_record()
    {
        int component2 = 0;
        if(a2 >=16 && a2<=30)
        {
            component2 = component2 +1;
        }
        else if(a2>=31 && a2<60)
        {
            component2 = component2 + 2;
        }
        else if(a2>=60)
        {
            component2 = component2 + 3;
        }
        component2 = component2 +a5_a-1;
        if(component2 ==0)
        {
            component2 = 0;
        }
        else if(component2>=1 && component2 <=2)
        {
            component2 = 1;
        }
        else if(component2>=3 && component2 <=4)
        {
            component2 = 2;
        }
        else if(component2>=5 && component2 <=6)
        {
            component2 = 3;
        }
        return component2;
    }
    public static int get_component3_record()
    {
        int component3 = 0;

        if(a4>=6 && a4<7)
        {
            component3 = component3 + 1;
        }
        else if(a4>5 && a4<6)
        {
            component3 = component3 + 2;
        }
        else if(a4<=5)
        {
            component3 = component3 + 3;
        }
        return component3;
    }
    public static int get_component4_record()
    {
        //a1이 취침 시간
        //a3이 일어난 시간
        int time_hour = 0;
        int time_min = 0;
        if(a3_hour > a1_hour)
        { //일어난 시간이 취침 시간보다 클 때
            time_hour = a3_hour - a1_hour;
        }
        else if(a3_hour < a1_hour)
        { //취침 시간이 일어난 시간보다 클 때
            time_hour = a3_hour - a1_hour;
            time_hour = 24 + time_hour;
        }
        else
        {//두 시간이 같을 때
            time_hour = 24;
            time_min = 0;
        }
        if(a3_min > a1_min)
        {
            time_min = a3_min - a1_min;
        }
        else if(a3_min < a1_min)
        {
            time_min =  60 +(a3_min - a1_min) ;
            time_hour = time_hour-1;
        }
        float temp1 = a4;
        float temp2 = time_hour * 60 + time_min;
        temp1 = (temp1/temp2) * 100;
        int result = (int)temp1;
        if(result >= 85)
        {
            return 0;
        }
        else if(result>=75 && result <=84)
        {
            return 1;
        }
        else if(result>=65 && result <=74)
        {
            return 2;
        }
        else if(result <65)
        {
            return 3;
        }
        return 0;
    }
    public static int for_test(int val)
    {
        int temp = val -1;
        int result = 0;
        switch (temp){
            case 0:
                result = 0;
                break;
            case 1:
                result = 1;
                break;
            case 2:
                result = 2;
                break;
            case 3:
                result = 3;
                break;
            default:
                break;
        }
        return result;
    }
    public static int get_component5_record()
    {
        int component5 =0;
        component5 = for_test(a5_b);
        component5 = component5 + for_test(a5_c);
        component5 = component5 + for_test(a5_d);
        component5 = component5 + for_test(a5_e);
        component5 = component5 + for_test(a5_f);
        component5 = component5 + for_test(a5_g);
        component5 = component5 + for_test(a5_h);
        component5 = component5 + for_test(a5_i);
        if(component5 == 0)
        {
            return 0;
        }
        else if(component5 >=1 && component5 <=9)
        {
            return 1;
        }
        else if(component5>=10 && component5 <=18)
        {
            return 2;
        }
        else if(component5>=19 && component5 <= 27)
        {
            return 3;
        }
        return 0;
    }
    public static int get_component7_record()
    {
        int component7 = for_test(a8) + for_test(a9);
        if(component7 ==0 )
        {
            return 0;
        }
        else if(component7>=1 && component7 <=2)
        {
            return 1;
        }
        else if(component7>=3 && component7<=4)
        {
            return 2;
        }
        else if(component7>=5 && component7 <=6)
        {
            return 3;
        }
        return 0;
    }
}
