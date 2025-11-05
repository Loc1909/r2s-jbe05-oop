package com.example.bikestores.util;

public class StringUtil {
    public static String truncate(String s, int len) {
        if (s == null)
            return null;
        if (s.length() <= len)
            return s;
        return s.substring(0, len - 3) + "...";
    }
}

