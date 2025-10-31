package com.example.session06;

import java.util.ArrayList;

public class Demo01 {
  public static void main(String[] args) {
    ArrayList<String> list = new ArrayList<>();

    list.add("apple");
    list.add("banana");
    list.add("orange");

    String searchElement = "apple";
    // Find the element (found or not found)
    // foreach
    for (String fruit : list) {
      if (searchElement.equals(fruit)) {
        System.out.println(searchElement + " found in the list.");
      }
    }
  }

}
