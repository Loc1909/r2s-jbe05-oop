package com.example.session06;

import java.util.ArrayList;

public class Demo02 {
  public static void main(String[] args) {
    ArrayList<String> list = new ArrayList<>();

    list.add("apple");
    list.add("banana");
    list.add("orange");

    String searchElement = "apple";
    // Find the element (found or not found)
    boolean found = list.stream().anyMatch(fruit -> fruit.equals(searchElement));
    if (found) {
      System.out.println("Found");
    } else {
      System.out.println("Not found");
    }
  }

}
