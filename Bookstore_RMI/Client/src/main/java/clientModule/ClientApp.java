package clientModule;


import clientModule.ui.Console;
import commonModule.service.BookServiceInterface;
import commonModule.service.ClientServiceInterface;
import commonModule.service.SaleServiceInterface;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class ClientApp {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(
                        "clientModule.config"
                );
        ClientServiceInterface clientServiceInterface = context.getBean(ClientServiceInterface.class);
        BookServiceInterface bookServiceInterface = context.getBean(BookServiceInterface.class);
        SaleServiceInterface saleServiceInterface = context.getBean(SaleServiceInterface.class);
        Console console = new Console(clientServiceInterface,bookServiceInterface,saleServiceInterface);
        console.runConsole();
    }
}
