package serverModule.config;

import commonModule.domain.Book;
import commonModule.domain.Client;
import commonModule.domain.Sale;
import commonModule.domain.validators.BookValidator;
import commonModule.domain.validators.ClientValidator;
import commonModule.domain.validators.SaleValidator;
import commonModule.domain.validators.Validator;
import commonModule.service.BookServiceInterface;
import commonModule.service.ClientServiceInterface;
import commonModule.service.SaleServiceInterface;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.remoting.rmi.RmiServiceExporter;
import serverModule.entityService.BookService;
import serverModule.entityService.ClientService;
import serverModule.entityService.SaleService;
import serverModule.repository.database_repository.BookDBRepository;
import serverModule.repository.database_repository.ClientDBRepository;
import serverModule.repository.database_repository.SaleDBRepository;
import serverModule.repository.database_repository.SortingRepository;
import serverModule.serverService.BookServiceServer;
import serverModule.serverService.ClientServiceServer;
import serverModule.serverService.SaleServiceServer;

@Configuration
public class AppConfig {

    @Bean
    ClientValidator clientValidator(){
        return new ClientValidator();
    }

    @Bean
    BookValidator bookValidator(){return new BookValidator();}

    @Bean
    SaleValidator saleValidator(){return new SaleValidator();
    }

    @Bean
    SortingRepository<Long,Client> clientDBRepository(){return new ClientDBRepository();}

    @Bean
    SortingRepository<Long,Book> bookDBRepository(){ return new BookDBRepository();
    }

    @Bean
    SortingRepository<Long, Sale> saleDBRepository(){return new SaleDBRepository();
    }

    @Bean
    BookService bookService(){return new BookService();}

    @Bean
    ClientService clientService(){ return new ClientService();}

    @Bean
    SaleService saleService(){ return new SaleService();}

    @Bean
    ClientServiceInterface clientServiceServer(){
        return new ClientServiceServer();
    }

    @Bean
    BookServiceInterface bookServiceServer(){
        return new BookServiceServer();
    }

    @Bean
    SaleServiceInterface saleServiceServer(){
        return new SaleServiceServer();
    }

    @Bean
    RmiServiceExporter rmiServiceExporterClient() {
        RmiServiceExporter rmiServiceExporter = new RmiServiceExporter();
        rmiServiceExporter.setServiceName("ClientService");
        rmiServiceExporter.setServiceInterface(ClientServiceInterface.class);
        rmiServiceExporter.setService(clientServiceServer());
        return rmiServiceExporter;
    }

    @Bean
    RmiServiceExporter rmiServiceExporterBook() {
        RmiServiceExporter rmiServiceExporter = new RmiServiceExporter();
        rmiServiceExporter.setServiceName("BookService");
        rmiServiceExporter.setServiceInterface(BookServiceInterface.class);
        rmiServiceExporter.setService(bookServiceServer());
        return rmiServiceExporter;
    }

    @Bean
    RmiServiceExporter rmiServiceExporterSale() {
        RmiServiceExporter rmiServiceExporter = new RmiServiceExporter();
        rmiServiceExporter.setServiceName("SaleService");
        rmiServiceExporter.setServiceInterface(SaleServiceInterface.class);
        rmiServiceExporter.setService(saleServiceServer());
        return rmiServiceExporter;
    }
}
