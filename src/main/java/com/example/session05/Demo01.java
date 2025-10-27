package com.example.session05;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Demo01 {

  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    int number = 0;

    try {
      System.out.print("Enter your number: ");
//      number = scanner.nextInt();
      number = Demo04.inputInteger();
    } catch (Exception exception) {
      System.out.println(exception.getMessage());
    }

    System.out.println("Your number is: " +number);
  }

}
