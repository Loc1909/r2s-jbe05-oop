package com.example.bikestores.util;

public class Constants {
    // Brand constants
    public static final String BRAND_HEADER = String.format("%-10s | %-30s", "Brand ID", "Brand Name");
    public static final String BRAND_ROW_FORMAT = "%-10d | %-30s";
    
    // Shipment display constants
    public static final String SHIPMENT_HEADER = String.format("%-10s | %-10s | %-25s | %-10s | %-10s | %-18s | %-12s | %-12s | %-12s",
            "Ship ID", "Order ID", "Address", "Method", "Carrier", "Tracking", "Status", "Shipped", "Delivered");
    public static final String SHIPMENT_ROW_FORMAT = "%-10d | %-10d | %-25s | %-10s | %-10s | %-18s | %-12s | %-12s | %-12s";
    
    // Shipment status constants
    public static final String STATUS_PENDING = "pending";
    public static final String STATUS_PROCESSING = "processing";
    public static final String STATUS_SHIPPED = "shipped";
    public static final String STATUS_DELIVERED = "delivered";
    public static final String STATUS_CANCELLED = "cancelled";
    public static final String STATUS_RETURNED = "returned";
    
    // Shipping method constants
    public static final String METHOD_STANDARD = "standard";
    public static final String METHOD_EXPRESS = "express";
    public static final String METHOD_PICKUP = "pickup";
    
    // Order status constants
    public static final String ORDER_STATUS_PAID = "PAID";
    
    // Shipment menu constants
    public static final int SHIPMENT_MENU_LIST = 1;
    public static final int SHIPMENT_MENU_SCHEDULE = 2;
    public static final int SHIPMENT_MENU_TRACK_ORDER = 3;
    public static final int SHIPMENT_MENU_TRACK_TRACKING = 4;
    public static final int SHIPMENT_MENU_UPDATE = 5;
    public static final int SHIPMENT_MENU_CANCEL = 6;
    public static final int SHIPMENT_MENU_DELETE = 7;
    public static final int MENU_BACK = 0;

    // Review display constants
    public static final String REVIEW_HEADER = String.format("%-10s | %-10s | %-10s | %-6s | %-40s | %-12s",
            "Review ID", "Customer", "Product", "Rating", "Comment", "Date");
    public static final String REVIEW_ROW_FORMAT = "%-10d | %-10d | %-10d | %-6d | %-40s | %-12s";

    // Rating constraints
    public static final int RATING_MIN = 1;
    public static final int RATING_MAX = 5;

    // Comment constraints
    public static final int COMMENT_MAX_LENGTH = 30;

    public static final int REVIEW_MENU_LIST = 1;
    public static final int REVIEW_MENU_SUBMIT = 2;
    public static final int REVIEW_MENU_UPDATE = 3;
    public static final int REVIEW_MENU_DELETE = 4;
}