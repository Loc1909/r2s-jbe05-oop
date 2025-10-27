public class Library {
  void displayBookInfo(Book book) {
    System.out.println("Title: " + book.getTitle());
    System.out.println("Author: " + book.getAuthor());
    System.out.println("Publication Year: " + book.getPublicationYear());
  }

  public static void main(String[] args) {
    Book book1 = new Book("To Kill a Mockingbird", "Harper Lee", 1960);
    Book book2 = new Book("1984", "George Orwell", 1949);
    Book book3 = new Book("The Great Gatsby", "F. Scott Fitzgerald", 1925);


    book1.setTitle("Java Programming Basics");

    Library library = new Library();
    library.displayBookInfo(book1);
    library.displayBookInfo(book2);
    library.displayBookInfo(book3);
  }
}
