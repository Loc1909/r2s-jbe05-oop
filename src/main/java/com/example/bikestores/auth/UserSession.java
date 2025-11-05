package com.example.bikestores.auth;

public class UserSession {
    private static int userId = 1;
    private static String username = "user";
    private static String role = "admin";

    public static void setUser(int id, String name, String r) {
        userId = id;
        username = name;
        role = r;
    }

    public static int getUserId() { return userId; }
    public static String getUsername() { return username; }
    public static String getRole() { return role; }

    public static boolean isLoggedIn() {
        return role != null;
    }

    public static boolean isAdmin() {
        return role.equalsIgnoreCase("admin");
    }

    public static void logout() {
        userId = 0;
        username = null;
        role = null;
    }
}