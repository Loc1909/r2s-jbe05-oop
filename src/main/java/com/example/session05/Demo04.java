package com.example.session05;

import java.util.Scanner;

public class Demo04 {

  public static int inputInteger() throws Exception {
    Scanner scanner = new Scanner(System.in);

    System.out.print("Enter the number: ");
    int number = Integer.parseInt(scanner.nextLine());

    if (number < 0 || number > 100) {
      throw new Exception("The number between 0 and 100");
    }

    return number;
  }

  public static void main(String[] args) {
    try {
      int num = inputInteger();
      System.out.println(num);
    } catch (Exception exception) {
      System.out.println(exception.getMessage());
    }

  }
}
