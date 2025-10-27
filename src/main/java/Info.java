import java.util.Scanner;

public class Info {

  public static void main(String[] args) {
    /*
    Nhập họ tên, tuổi, địa chỉ
    In ra thông tin theo định dạng:
    Họ tên: <họ tên>
    Tuổi: <tuổi>
    Địa chỉ: <địa chỉ>
     */

    String name = "an";
    name = name + "hihi";
    String name2 =new String("an");

    System.out.println(name == name2); // false
    System.out.println(name.equals(name2)); // true

//    int age;
//    String address;
//
//    // inpput
//    Scanner scanner = new Scanner(System.in);
//
//    System.out.print("Enter your name: ");
//    name = scanner.nextLine();
//
//    System.out.print("Enter your age: ");
//    age = Integer.parseInt(scanner.nextLine()) ;
//
//    System.out.print("Enter your address: ");
//    address = scanner.nextLine(); // consume the leftover newline
//
//    // output
//    System.out.println("Ho ten: " + name);
//    System.out.println("Tuoi: " + age);
//    System.out.println("Dia chi: " + address);
  }
}
