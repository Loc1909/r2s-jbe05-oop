package com.example.bikestores.util;

public class ValidationUtil {
    public static boolean isValidString(String input) {
        return input != null && !input.trim().isEmpty();
    }
}