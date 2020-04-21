package bookstore_pck;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import bookstore_pck.ui.Console;


public class Main {

    public static void main(String[] args) {
        System.out.println("Hello :D");

        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(
                        "bookstore_pck"
                );

        context.getBean(Console.class).runConsole();

        System.out.println("bye");

    }
}
