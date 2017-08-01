import java.util.*;

public class test {

   public static void main(String[] args) {

   // create a LinkedList
   LinkedList list = new LinkedList();

   // add some elements
   list.add("Hello");
   list.add(2);
   list.add("Chocolate");
   list.add("10");

   // print the list
   System.out.println("LinkedList:" + list);

   // add a new element at the end of the list
   list.add(2,"Element");

   // print the updated list
   System.out.println("LinkedList:" + list);
   }
}