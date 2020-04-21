package serverModule;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;


public class ServerApp {

    public static void main(String[] args) {
        System.out.println("\t-----Server Has Started-----");
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(
                        "serverModule.config"
                );
    }

}
