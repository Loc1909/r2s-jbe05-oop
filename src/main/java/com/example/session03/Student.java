package com.example.session03;

import java.util.Scanner;

public class Student extends Person {
  private float gpa;

  public Student(String id, String name, String email, float gpa) {
    super(id, name, email);
    this.gpa = gpa;
  }

  @Override
  public void inputInfo() {
    super.inputInfo();
  }
}
