package com.example.bikestores.util;

import java.time.LocalDate;

public class ValidationUtil {
    public static boolean isValidString(String input) {
        return input != null && !input.trim().isEmpty();
    }

    public static int readRating(String prompt, int min, int max) {
        while (true) {
            int rating = ScannerUtil.readInt(prompt);
            if (rating >= min && rating <= max) {
                return rating;
            }
            System.out.println("Invalid rating. Try again.");
        }
    }

    public static String readShippingMethod(String prompt) {
        while (true) {
            String method = ScannerUtil.readNonEmptyString(prompt).toLowerCase();
            if (method.equals(Constants.METHOD_STANDARD) || 
                method.equals(Constants.METHOD_EXPRESS) || 
                method.equals(Constants.METHOD_PICKUP)) {
                return method;
            }
            System.out.println("Invalid method. Choose standard/express/pickup.");
        }
    }

    public static String readShipmentStatus(String prompt) {
        while (true) {
            String status = ScannerUtil.readNonEmptyString(prompt).toLowerCase();
            if (status.equals(Constants.STATUS_PENDING) || 
                status.equals(Constants.STATUS_PROCESSING) ||
                status.equals(Constants.STATUS_SHIPPED) || 
                status.equals(Constants.STATUS_DELIVERED) ||
                status.equals(Constants.STATUS_RETURNED) || 
                status.equals(Constants.STATUS_CANCELLED)) {
                return status;
            }
            System.out.println("Invalid status. Choose one of the listed values.");
        }
    }

    public static LocalDate readDateOrNull(String prompt) {
        while (true) {
            String dateStr = ScannerUtil.readNonEmptyString(prompt);
            if ("null".equalsIgnoreCase(dateStr)) {
                return null;
            }
            try {
                return LocalDate.parse(dateStr.trim());
            } catch (Exception e) {
                System.out.println("Invalid date format. Use yyyy-mm-dd or type 'null'.");
            }
        }
    }

    public static String readComment(String prompt, int maxLength) {
        while (true) {
            String comment = ScannerUtil.readNonEmptyString(prompt);
            if ("null".equalsIgnoreCase(comment)) {
                return null;
            }
            if (comment.length() <= maxLength) {
                return comment;
            }
            System.out.println("Comment exceeds maximum length of " + maxLength + " characters. Please shorten your comment.");
        }
    }
}