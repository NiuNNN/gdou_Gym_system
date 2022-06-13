package com.gdou.utils;

public class PriceUtil {
    public static Float strFormatDouble(String price){
        String substring = price.substring(1);
        String[] split = substring.split("/");
        return Float.valueOf(split[0]);
    }
}
